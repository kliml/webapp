# webapp
Simple Java servlet container with full servlet lifecycle.
Request and response classes for servlets are not fully implemented, because purpose of this project is to demonstrate configurable application which can be used to mock different webservices.
## Adding a servlet
To add a servlet:
- Place servlet in `servlet` package.
- Add servlet URL and servlet class name to `servlet.properties`.
- Add any required setting to `config.properties` and extend `util.Setting` for parsing them.
- Check that all required methods are implemented in `container.Request` and `container.Response`.
## Included servlets
Included servlets only accept US codes due to limitations of free APIs that are used.
Servlets return full response from API calls because their purpose is only to demonstrate servlet performance.
- Weather service: returns weather information by city code passed as a parameter in the query string.
\nExample query: GET `http://localhost:8080/weather?city=2172797`
\nOutput:
```
{
    "coord": {
        "lon": 145.77,
        "lat": -16.92
    },
    "weather": [
        {
            "id": 803,
            "main": "Clouds",
            "description": "broken clouds",
            "icon": "04n"
        }
    ],
    "base": "stations",
    "main": {
        "temp": 298.15,
        "feels_like": 300.81,
        "temp_min": 298.15,
        "temp_max": 298.15,
        "pressure": 1014,
        "humidity": 78
    },
    "visibility": 10000,
    "wind": {
        "speed": 2.1,
        "deg": 160
    },
    "clouds": {
        "all": 55
    },
    "dt": 1605529360,
    "sys": {
        "type": 1,
        "id": 9490,
        "country": "AU",
        "sunrise": 1605468865,
        "sunset": 1605515352
    },
    "timezone": 36000,
    "id": 2172797,
    "name": "Cairns",
    "cod": 200
}
```

- Geo-location service: returns time zone by ZIP code given in the POST.
\nExample query: POST `http://localhost:8080/location?zip=99501`, POST body: `99501`
\nOutput:
```
{
    "zip_code": "99501",
    "lat": 61.219969,
    "lng": -149.855962,
    "city": "Anchorage",
    "state": "AK",
    "timezone": {
        "timezone_identifier": "America/Anchorage",
        "timezone_abbr": "AKST",
        "utc_offset_sec": -32400,
        "is_dst": "F"
    },
    "acceptable_city_names": [],
    "area_codes": [
        907
    ]
}
```
## What can be improved
- Full implementation for request and response classes.
- Better error handling.
- Logging.
- Buffer for HTTP request body.
- Store API more securely.
- Tests.
- Checking POST body content type.

