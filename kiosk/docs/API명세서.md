### 주요 기능 API 명세서



1. 고객 등록 api
    - url path: POST /v1/user/new
    - request:
        1. name, String
        2. password, String (4자리)
    - response:
        1. 성공 (200) 
           - "success"
        2. 실패
           - 없음


2. 고객 카드 발급 api
    - url path: POST /v1/user/card/new
    - request:
        1. name, String (겹치는 이름 없다고 우선 가정)
        2. password, String (4자리)
    - response:
        1. 성공 (200)
            - cardNumber, String (중복 없는 16자리)
        2. 실패
            - 고객 이름이 없는 경우 -> 400
            - 비밀번호가 틀린 경우 -> 400

    
3. 고객 카드 번호 조회 api
   - url path: GET /v1/user/card/card-number
   - request:
        1. name, String (겹치는 이름 없다고 우선 가정)
        2. password, String (4자리)
   - response:
        1. 성공 (200)
           - cardNumber, String (중복 없는 16자리)
        2. 실패
            - 고객 이름이 없는 경우 -> 400
            - 비밀번호가 틀린 경우 -> 400
        

4. 고객 카드 충전 api
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

    
5. 고객 카드 잔액 확인 api
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


6. 영화 추가 api
    - url path: GET /v1/movie/new
    - request:
        1. movieList, List<MovieRequestModel>
           - title, String
           - runningTime, Int
           - releaseDate, LocalDate
           - endDate, LocalDate
           - price, Int
    - response:
        1. 성공 (200)
            - List<MovieEntity>
        2. 실패
            - 없음
   

7. 원하는 날마다 영화 상영표 생성 api
    - url path: GET /v1/movie/daily-showtimes
    - request:
      - date, LocalDate
    - response:
        1. 성공 (200)
            - List<MovieShowtimesEntity>
            - emptyList, List<T>
        2. 실패
            - 없음


8. 영화 상영표 확인 api
    - url path: GET /v1/movie/showtimes/date
    - request:
        1. date, LocalDate
    - response:
        1. 성공 (200)
            - List<MovieShowtimesWithSeatsDto>
            - emptyList, List<T>
        2. 실패
            - 없음


9. 영화 티켓 예매 api
    - url path: POST /v1/movie/ticket
    - request:
        1. cardNumber, String (중복 없는 16자리)
        2. password, String (4자리)
        3. date: LocalDate
        4. title, String
        5. seatNumber, String 
        6. startTime, String (09:00 (O), 9:00 (X))
    - response:
        1. 성공 (200)
            - "감사합니다. ~~~~ 예약 완료 되었습니다."
        2. 실패
            - 카드 번호가 없는 경우 -> 400
            - 비밀번호가 틀린 경우 -> 400
            - 입력한 영화 제목을 통해 영화 정보 찾을 수 없는 경우 -> 400
            - 카드 잔액이 부족한 경우 -> 400
            - 입력한 좌석이 빈자리가 아닌 경우 -> 400


10. 극장 매니저 일일 매출 현황 api
    - url path: GET /v1/manager/daily-sales/{date}
    - request:
        1. date, Date
    - response:
        1. 성공 (200)
            - Map<String, Int?>
              - 카드 충전 금액
              - 티켓 판매 금액
              - 총 매출 금액
        2. 실패
            - 없음


    
