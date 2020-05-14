# miro-widgets

Swagger are accessible via the link http://localhost:8081/swagger-ui.html


# Configuration
Rate limits could be managed in `application.yml` for each endpoint, default is 1000 requests per minute
Zuul was selected as a rate limiter. 

Redis was selected as a storage of rate limits per each endpoint.
```
zuul:
  routes:
    widgetsAll:
      path: /api/v1/widgets
      url: forward:/
    default:
      path: /api/v1/widgets/**
      url: forward:/
  ratelimit:
    enabled: true
    repository: bucket4j_ignite
    policy-list:
      widgetsAll:
        - limit: 200
          refresh-interval: 60
          type:
            - origin
      default:
        - limit: 1000
          refresh-interval: 60
          type:
            - origin
  strip-prefix: true
```              
InMemory or Database implementation (H2) can be selected in ``application.yml`` 

```

service:
  class: InMemory
#  class: H2Service

```       