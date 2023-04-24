### 주요 기능 API 명세서



1. 고객 등록 api
    - url path: POST /v1/user/new
    - request:
        1. name, String
        2. password, String (4자리)
    - response:
        1. 없음


2. 고객 카드 발급 api
    - url path: POST /v1/user/card
    - request:
        1. name, String (겹치는 이름 없다고 우선 가정)
        2. password, String (4자리)
    - response:
        1. 성공 (200)
            - cardNumber, String (중복 없는 16자리)
        2. 실패
            - 고객 이름이 없는 경우 -> 400
            - 비밀번호가 틀린 경우 -> 400

**(고객 카드 번호 조회 api 필요)

3. 고객 카드 충전 api
    - url path: POST /v1/user/card/money
    - request:
        1. cardNumber, String (중복 없는 16자리)
        2. password, String (4자리)
        3. amount, Int (5만원 단위라 5의 배수로만)
    - response:
        1. 성공 (200)
            - balance, Int
        2. 실패
            - 카드 번호가 없는 경우 -> 400
            - 비밀번호가 틀린 경우 -> 400
            - amount 입력 단위가 5의 배수가 아닌 경우 -> 400



4. 고객 카드 잔액 확인 api
    - url path: POST /v1/user/card/balance
    - request:
        1. cardNumber, String (중복 없는 16자리)
        2. password, String (4자리)
    - response:
        1. 성공 (200)
            - balance, Int
        2. 실패
            - 카드 번호가 없는 경우 -> 400
            - 비밀번호가 틀린 경우 -> 400



5. 영화 상영표 확인 api
    - url path: GET /v1/movie/showtimes
    - request:
        1. 없음
    - response:
        1. 성공 (200)
            - ListOfMovies, List<Movie>?
        2. 실패
            - 없음


6. 영화 티켓 예매 api
    - url path: POST /v1/movie/ticket
    - request:
        1. cardNumber, String (중복 없는 16자리)
        2. password, String (4자리)
        3. title, String
        4. seats, Int
    - response:
        1. 성공 (200)
            - 예약 성공 메세지
        2. 실패
            - 카드 번호가 없는 경우 -> 400
            - 비밀번호가 틀린 경우 -> 400
            - 입력한 title을 통해 영화 정보 찾을 수 없는 경우 -> 400
            - 입력한 seats가 빈자리가 아닌 경우 -> 400


7. 극장 매니저 일일 매출 현황 api
    - url path: GET /v1/manager/daily-sales/{date}
    - request:
        1. date, Date
    - response:
        1. 성공 (200)
            - dailyCardTopUpAmount, Int
            - dailyTicketSalesAmount, Int
        2. 실패
            - 없음


    
