# 🎮 **League Service**  
### *Spring Boot Microservice for League of Legends Data*

This project is a **League Service** built using **Spring Boot** that integrates with the Riot API to provide detailed League of Legends game data through REST APIs.

---

## 🔧 **Key Features**
- ✅ Fetch summoner info by Riot ID  
- ✅ Retrieve detailed match information  
- ✅ List match IDs by PUUID  
- ✅ Get champion usage statistics  
- ✅ Fetch champion mastery details  
- ✅ Interactive Swagger UI for API exploration  

---

## 🗂️ **Available REST Endpoints**

| Method | Endpoint                                                                                  | Description                        |
|--------|-------------------------------------------------------------------------------------------|----------------------------------|
| GET    | `/api/league/summoner/{region}/summoner-info/{gameName}/{tagLine}`                        | Get summoner info by gameName and tagline      |
| GET    | `/api/league/summoner/{region}/matches/{matchId}`                                        | Get match details by matchId                 |
| GET    | `/api/league/summoner/{region}/matches/by-puuid/{puuid}/ids`                             | Get match IDs by PUUID            |
| GET    | `/api/league/summoner/{region}/champion-usage/{gameName}/{tagLine}`                      | Get champion usage statistics gameName and tagline  |
| GET    | `/api/league/summoner/{region}/champion-mastery/by-riot-id/{gameName}/{tagLine}`         | Get champion mastery by gameName and tagline  |

---

## 🚀 **Swagger**
http://localhost:8082/api/league/swagger-ui.html
