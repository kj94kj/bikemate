package com.example.capstonemap;

import static com.example.capstonemap.locationUpdate.LocationUpdateTime.locationRequestSetting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.provider.ProviderProperties;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.capstonemap.currentMapBound.CurrentMapBound;
import com.example.capstonemap.currentMapBound.OnBoundListener;
import com.example.capstonemap.currentMapBound.SearchLocation;
import com.example.capstonemap.distance.DistanceTwoLocation;
import com.example.capstonemap.locationUpdate.GetOldRecord;
import com.example.capstonemap.polyLine.ClickPolyLine;
import com.example.capstonemap.polyLine.PolyLine;
import com.example.capstonemap.databinding.ActivityMapsBinding;
import com.example.capstonemap.locationUpdate.UserUpdateInfo;
import com.example.capstonemap.routes.DeleteRoute;
import com.example.capstonemap.routes.FilterDialog;
import com.example.capstonemap.routes.GetRoutes;
import com.example.capstonemap.routes.RouteDto;
import com.example.capstonemap.user.GetUserRoutes;
import com.example.capstonemap.user.UserDto;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lombok.Getter;

// 폴리라인 그리기가 적절히 들어간건아님.
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, OnBoundListener {

    private static final double MOCK_SPEED_M_S = 4.167; // 시속 15km/h를 m/s로 변환
    private static final String MOCK_PROVIDER = "mockProvider";
    private static GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Marker oldLocationMarker = null;
    private SearchLocation searchLocation;

    // 모든 루트를 저장하는 dto
    private List<RouteDto> allRouteDtoList = new ArrayList<>();

    // 현재 맵 범위에 있는 루트를 받는 dto
    private List<RouteDto> boundRouteDtoList = new ArrayList<>();

    // 지정한 길이의 모든 routeDtoList를 받는 dto
    private List<RouteDto> allLengthRouteDtoList = new ArrayList<>();

    // 루트에 있을 때만 UserUpdateInfo에 좌표값, 속도값을 넣으려고함.
    // RouteInOut과 연관된 변수.
    public static boolean isRouteSelected = false;
    public static boolean isInRoute = false;
    public static boolean isRacing = false;

    // 일단 userId = 1, routeId = 1로 설정함.
    // 사실은 유저가 바뀔때마다 루트가 바뀔때마다 다르게 설정해줘야함.
    @Getter
    private static UserUpdateInfo userUpdateInfo;

    private double latitude = 37.506632;  // 시작 위도
    private double longitude = 126.960733;  // 시작 경도
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 모든 루트 담음.
        allRouteDtoList = new ArrayList<>();
        allRouteDtoList = GetRoutes.getAllRoutes();

        // FusedLocationProviderClient 및 LocationRequest 설정
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = locationRequestSetting();

        // 루트 안에 있다고 판단하면 UserUpdateInfo 객체를 생성하고, userUpdateInfo의 oldMyRecord를 지정해주는 것.
        // 루트를 고름 -> 자기 기록 불러옴
        // 기록이 없으면 발생할 것도 만들어야함
        if(isRouteSelected){
            userUpdateInfo = new UserUpdateInfo(1L, 1L);
            userUpdateInfo.setOldMyRecord(GetOldRecord.getMyOldRecord(1L, 1L));
        }

        // 경주모드면 내가 선택한 기록을 가져오게함
        // 현재는 내 과거기록을 가져오게함
        if(isRouteSelected && isRacing){
            userUpdateInfo.setOldOtherRecord(GetOldRecord.getOldRecord(1L, 1L));
        }

        // LocationCallback 설정
        // LocationCallback을 통해서 현재 나의 위치 속도 등을 받아올 수 있다.
        // 빼서 저장 해도 될듯.
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null ) return;

                for (Location location : locationResult.getLocations()) {
                    Double[] currentLocation = {location.getLatitude(), location.getLongitude()};
                    double speed = location.getSpeed();

                    // 루트 안에 있을 때만 userUpdateInfo를 갱신함.
                    if(isInRoute){
                        userUpdateInfo.setLocation(currentLocation);
                        userUpdateInfo.setSpeed(speed);
                    }else{
                        if(userUpdateInfo != null){
                            userUpdateInfo=null;
                        }
                    }

                    // 현재 순위 1위인지 2위인지 알려줌, 고스트 유저 위치를 set함. 화면에 보여주는 컴포넌트도 필요함.
                    if(isRacing){

                        userUpdateInfo.setOldLocation();
                        int nowRanking= userUpdateInfo.raceRankingNow();
                        Double[] oldLocation = (userUpdateInfo.getOldLocation());
                        LatLng oldLatLng = DistanceTwoLocation.doubleToLatLng(oldLocation);

                        if (oldLocationMarker != null) {
                            oldLocationMarker.remove();
                        }

                        oldLocationMarker=mMap.addMarker(new MarkerOptions().position(oldLatLng));
                        System.out.println(nowRanking);
                    }
                }
            }
        };

        // LocationManager 초기화 및 모의 위치 제공자 설정
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        setupMockProvider();

        // GoogleMap 초기화 요청
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // 버튼 눌러서 minLength maxLength받고 그 범위안에 있는걸 routeDto에 담아서 보여주던가함.
        findViewById(R.id.filter_button).setOnClickListener(v -> {
            FilterDialog.showSeekBarDialog(this, (minLength, maxLength) -> {
                applyFilter(minLength, maxLength);
            });
        });

        // 검색창관련
        searchLocation = new SearchLocation(this);
        setupSearchListener();

        Button buttonRouteManagement = findViewById(R.id.button_route_management);
        buttonRouteManagement.setOnClickListener(v -> {
            Intent intent = new Intent(MapsActivity.this, RouteManagementActivity.class);
            startActivity(intent);
        });

        // UI 요소
        Button toggleButton = findViewById(R.id.toggle_button); // XML에서 정의된 버튼
        View filterButton = findViewById(R.id.filter_button); // visibility를 토글할 버튼

        // 가시성 토글 이벤트 설정
        toggleButton.setOnClickListener(v -> {
            if (filterButton.getVisibility() == View.VISIBLE) {
                filterButton.setVisibility(View.GONE);
            } else {
                filterButton.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        enableMyLocation();

        mMap.getUiSettings().setZoomControlsEnabled(true);

        //임시적인 유저생성 지워야함.
        UserDto userDto=new UserDto(1L, "a", "a");

        if(allLengthRouteDtoList != null && !allLengthRouteDtoList.isEmpty()){
            CurrentMapBound.cameraBoundListener(mMap, allLengthRouteDtoList, this);
        }else if(allRouteDtoList != null && !allRouteDtoList.isEmpty()){
            CurrentMapBound.cameraBoundListener(mMap, allRouteDtoList, this);
        }


        // 지도에 기본 마커 추가
        LatLng location = new LatLng(37.506632, 126.960733);
        mMap.addMarker(new MarkerOptions().position(location).title("Jung-dae Hospital"));

        // 클릭 이벤트 설정
        ClickPolyLine.clickPolyLine(mMap, userDto);


        // 위치 업데이트 시작
        startLocationUpdates();

        // 모의 이동
        startMockMovement();

        //AllRoutesButton(임시), User의 id를 매개변수로 받는 getUesrRoutesButton(임시)
        GetRoutes.getAllRoutesButton(binding);
        GetUserRoutes.getUserRoutesButton(binding, userDto.getId());

        DeleteRoute.deleteButton(binding,1L, 1L);

        // 버튼의 visibility 설정
        // binding.GetAllRoutesButton.setVisibility(View.GONE);
    }

    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            try {
                if (mMap != null) {
                    mMap.setMyLocationEnabled(true);
                }
            } catch (SecurityException e) {
                Log.e("MapsActivity", "Location permission denied", e);
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupMockProvider() {
        try {
            if (!locationManager.isProviderEnabled(MOCK_PROVIDER)) {
                locationManager.addTestProvider(MOCK_PROVIDER, false, false, false, false, true,
                        true, true, ProviderProperties.POWER_USAGE_HIGH, ProviderProperties.ACCURACY_COARSE);
                locationManager.setTestProviderEnabled(MOCK_PROVIDER, true);
                Log.d("MapsActivity", "Mock provider added and enabled");
            }
        } catch (SecurityException e) {
            Toast.makeText(this, "Mock location permission is required", Toast.LENGTH_SHORT).show();
        }
    }

    private void startMockMovement() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 이동 경로를 시뮬레이션하여 경도와 위도를 조금씩 변화
                latitude += 0.00001;  // 이동량 조정
                longitude += 0.00001;

                // 모의 위치 생성 및 설정
                Location mockLocation = new Location(MOCK_PROVIDER);
                mockLocation.setLatitude(latitude);
                mockLocation.setLongitude(longitude);
                mockLocation.setAccuracy(1.0f);
                mockLocation.setSpeed((float) MOCK_SPEED_M_S); // 시속 15km/h로 설정
                mockLocation.setTime(System.currentTimeMillis());
                mockLocation.setElapsedRealtimeNanos(System.nanoTime());  // 시스템 타임 추가

                try {
                    // 모의 위치 업데이트 전송
                    locationManager.setTestProviderLocation(MOCK_PROVIDER, mockLocation);
                    updateMockLocation(mockLocation.getLatitude(), mockLocation.getLongitude());
                    Log.d("MapsActivity", "Mock location updated: Lat=" + latitude + ", Lng=" + longitude);
                }  catch (SecurityException e) {
                    Log.e("MapsActivity", "SecurityException: Mock location permission not granted", e);
                } catch (IllegalArgumentException e) {
                    Log.e("MapsActivity", "IllegalArgumentException: Issue with mock location provider", e);
                }

                // 1초마다 업데이트
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            try {
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
            } catch (SecurityException e) {
                Log.e("MapsActivity", "Location permission denied for location updates", e);
                Toast.makeText(this, "Location permission denied for location updates", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            locationManager.removeTestProvider(MOCK_PROVIDER);  // 사용한 테스트 프로바이더의 이름에 따라 변경
        } catch (SecurityException e) {
            e.printStackTrace();
            // 예외가 발생한 경우 Mock Location 권한이 없음을 의미
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Location permission was not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static GoogleMap getMap() {
        return mMap;
    }


    // 여기서 부터 모의로 속도 구하는 함수
    private Location previousLocation;
    private long previousTime;

    private void updateMockLocation(double latitude, double longitude) {
        Location currentLocation = new Location(MOCK_PROVIDER);
        currentLocation.setLatitude(latitude);
        currentLocation.setLongitude(longitude);
        currentLocation.setTime(System.currentTimeMillis());

        // 이전 위치가 있다면 속도를 계산
        if (previousLocation != null) {
            float distance = previousLocation.distanceTo(currentLocation); // 거리(m)
            long timeDelta = currentLocation.getTime() - previousTime; // 시간 차이(ms)

            if (timeDelta > 0) {
                float speed = (distance / timeDelta) * 1000; // m/s 단위로 변환
                float speedKmH = speed * 3.6f; // km/h 단위로 변환
                Log.d("MapsActivity", "Calculated speed: " + speedKmH + " km/h");

                binding.speedTextView.setText(String.format(Locale.US, "Speed: %.2f km/h", speedKmH));
            }
        }

        // 현재 위치와 시간을 이전 위치로 저장
        previousLocation = currentLocation;
        previousTime = currentLocation.getTime();
    }

    //userUpdateInfo를 set하는 함수 다른 userId와 routeId를 받을 수 있도록 바꿔야한다.
    public static void setUserUpdateInfo() {
        userUpdateInfo = new UserUpdateInfo(1L, 1L);
        userUpdateInfo.setStartedTime(System.currentTimeMillis());
    }

    public static void setEndUserUpdateInfo(){
        userUpdateInfo.setElapsedTime((System.currentTimeMillis() - userUpdateInfo.getStartedTime()) / 1000);

        userUpdateInfo.setUserRecordDto();
    }


    // 이걸 코스아이템에 담아야함. boundRouteDtoList -> 현재 맵 화면 안에 시작점이 있는 루트들을 가져옴.
    @Override
    public void onBound(List<RouteDto> boundRouteDtoList) {
        this.boundRouteDtoList.clear();
        this.boundRouteDtoList.addAll(boundRouteDtoList);
    }

    // 검색창관련
    private void setupSearchListener() {
        SearchView searchView = findViewById(R.id.search_location);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                LatLng latLng = searchLocation.getCoordinates(query);

                if (latLng != null) {
                    // 지도 이동
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                    // 마커 추가
                    mMap.addMarker(new MarkerOptions().position(latLng).title(query));

                    Toast.makeText(MapsActivity.this, "Moved to " + query, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MapsActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    // minLength, maxLength를 받고, 그 루트 길이 안에 있는 모든 루트를 가져옴.
    private void applyFilter(double minLength, double maxLength) {
        // 전체 루트를 거리 조건으로 필터링
        allLengthRouteDtoList  = GetRoutes.getLengthRoutes(minLength, maxLength);

        PolyLine.removeAllPolylines();
    }
}