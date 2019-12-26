# OutStagram
- `Spring Web-Flux`를 이용한 `REST-API SNS` 서버를 만드는 저장소입니다. 

## 준비사항
- `jdk` (>=11)
- `Gradle` or `IntelliJ`
- `Spring-boot` (>=2.x)
- `MongoDB` (>=1.19)

## 실행 방법
1. `DataBase`
    - `Download` [Robo 3T](https://robomongo.org/download) 
    -  `MongoDB Connection → Connect`
    - `Create Data Base → Database Name: OutStagram → Create`
    
2. `InteliJ` 
    - `File → New → Project from Version Control → Git`
    - `URL : https://github.com/team-ksu-14/OutStagram.git → Clone`
    
3. `Run BootApplication` 

## API 스펙

<details>
<summary>펼치기</summary>
<div markdown="1">

### 회원
회원을 조회하거나 생성할 때 사용합니다.

#### 회원 목록 조회
`GET` 요청을 사용하여 가입되어 있는 모든 회원을 조회할 수 있습니다.

##### 요청 예시
`$ curl 'http://localhost:8080/api/members' -i -X GET`

#### HTTP 요청
~~~
GET /api/members HTTP/1.1
Host: localhost:8080`
~~~

#### 응답 예시
~~~
[
    {
        "id": "5df7682f89b97d5fa516276c",
        "email": "test@email.com",
        "username": "testuser",
        "password": "{bcrypt}$2a$10$J9j9vjxiY2uBS2RtQpesR.bTwNuS3oKojssvKWbIrUaqdUmS/yAQy",
        "image": null,
        "bio": null,
        "createdAt": "2019-12-16T20:19:10.916",
        "updatedAt": null,
        "roles": [
            "USER",
            "ADMIN"
        ],
        "accessToken": null
    },
    {
        "id": "5df7682f89b97d5fa516276d",
        "email": "test2@email.com",
        "username": "testuser2",
        "password": "{bcrypt}$2a$10$edPebMKzbe/5jPXhZDhy4efOzurHKwJxZEhEsP5GTm/tV7pahkqdO",
        "image": null,
        "bio": null,
        "createdAt": "2019-12-16T20:19:10.916",
        "updatedAt": null,
        "roles": [
            "USER"
        ],
        "accessToken": null
    },
    {
        "id": "5df7682f89b97d5fa516276e",
        "email": "test3@email.com",
        "username": "testuser3",
        "password": "{bcrypt}$2a$10$Icp9CcFyodc3nb3LqbtKVuVT5yY9Q9SNO0KKtgHUmWwGx7ujYYEmW",
        "image": null,
        "bio": null,
        "createdAt": "2019-12-16T20:19:10.916",
        "updatedAt": null,
        "roles": [
            "ADMIN"
        ],
        "accessToken": null
    },
    {
        "id": "5df7682f89b97d5fa516276f",
        "email": "authTest@test.com",
        "username": "authTest",
        "password": "{bcrypt}$2a$10$hM7O4KNCwqpXpNw406CuQ.0ErfwWeslOKoP0Fupxw6byyHA7rs1ke",
        "image": null,
        "bio": null,
        "createdAt": "2019-12-16T20:19:10.917",
        "updatedAt": null,
        "roles": [
            "USER",
            "ADMIN"
        ],
        "accessToken": null
    }
]
~~~

#### 응답 필드
|Field|Type|Description|
|:---:|:---:|:---:|
|id|String|번호|
|email|String|이메일|
|username|String|이름|
|password|String|비밀번호|
|image|String|프로필 사진|
|bio|String|소개|
|createdAt|LocalDateTime|생성 시간|
|updatedAt|LocalDateTime|수정 시간|
|roles|Set|권한|
|accessToken|String|인증 토큰|

#### 회원 생성
`POST` 요청을 사용하여 회원을 생성할 수 있습니다.

##### 요청 예시
~~~
$ curl 'http://localhost:8080/api/members/' -i -X POST \
    -H 'Content-Type: application/json;charset=UTF-8' \
    -H 'Accept: application/json;charset=UTF-8' \
    -d '{
    "email": "testEmail@gmail.com",
    "password": "testPassword1!"
}'
~~~

#### HTTP 요청
~~~
POST /api/members HTTP/1.1
Host: localhost:8080
Content-Type: application/json
cache-control: no-cache
{
	"email": "asdf@email.com",
	"username": "test",
	"password": "adsf123!"
}
~~~

#### 응답 예시
~~~
{
    "id": "5df76bed89b97d5fa5162770",
    "email": "asdf@email.com",
    "username": "test",
    "password": "{bcrypt}$2a$10$YcX7cgJFBGiEliNC0N3uf.BDaF.6ooTuqkH/Yz7.gRIIfTxtB6j1O",
    "image": null,
    "bio": null,
    "createdAt": null,
    "updatedAt": null,
    "roles": [
        "USER"
    ],
    "accessToken": null
}
~~~

#### 응답 필드
|Field|Type|Description|
|:---:|:---:|:---:|
|id|String|번호|
|email|String|이메일|
|username|String|이름|
|password|String|비밀번호|
|image|String|프로필 사진|
|bio|String||
|createdAt|LocalDateTime|생성 시간|
|updatedAt|LocalDateTime|수정 시간|
|roles|Set|권한|
|accessToken|String|인증 토큰|

### 게시물
- 추가 예정

</div>
</details>