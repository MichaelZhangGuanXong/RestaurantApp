# RestaurantApp
A example code for restful API to book/lookup reservation by using mu-server dependency

2 API's can run: Http get to lookup restaurant reservations by day; Http post to book a reservation

1 Repository to simulate JPA repository, just like save/lookup from DB

Testing can be implemented by Postman like: (clone project, open in IntelliJ, the run RestaurantApp in your local machine)

Lookup by day - url get - https://localhost:8080/reservations/{yyyy-mm-dd}
![image](https://github.com/MichaelZhangGuanXong/RestaurantApp/assets/155645589/92560dcf-999b-4b7d-be80-daa3d2c2d384)

Book: url post - https://localhost:8080/reservations

![image](https://github.com/MichaelZhangGuanXong/RestaurantApp/assets/155645589/20163723-7d52-4b29-b464-51ce19b42e1b)

Also can test throught built-in page - https://localhost:8080/index.html
![image](https://github.com/MichaelZhangGuanXong/RestaurantApp/assets/155645589/d0c109dd-5577-4b07-932a-62a7471fd7ae)

Due to mu server support OpenAPI, so also can open it  - https://localhost:8080/api.html
![image](https://github.com/MichaelZhangGuanXong/RestaurantApp/assets/155645589/7b0e8353-d723-4c0b-8611-fc6baaf6aa68)


