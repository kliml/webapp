# webapp
Simple Java servlet container with full servlet lifecycle.
Request and response classes for servlets are not fully implemented, because purpose of the project is to demonstrate configurable application which can be used to mock different webservices.
## Adding a servlet
To add a servlet:
- Place servlet in servlet package.
- Add servlet URL and servlet class name to `servlet.properties`.
- Add any required setting to `config.properties` and extend `util.Setting` for parsing them.
- Check that all required methods are implemented in `container.Request` and `container.Response`.
## Included servlets
- Weather service: returns weather information by city code passed as a parameter in the query string.
- Geo-location service: returns time zone by ZIP code given in the POST.