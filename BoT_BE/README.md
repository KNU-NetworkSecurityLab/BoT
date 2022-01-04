# BoT(Blockchain of Things) - 블록체인을 이용한 IoT 보안 고도화
# Back-end(Hyperledger Fabric Network, chaincoe ...)
## Precondition - 필요한 것들
* OS: Ubuntu(18.04)
* Tools/Software: 
  * cURL
  * docker
  * docker-compose
  * Go
  * Node.js
  * Python
* Enviroments:
  * GOPATH are configured
  * Hyperledger Fabric-docker images need to be installed(1.4.3)
  * Hyperledger bineries are installed(crytogen, configtxgen, ...) <- means bin files
  
## Description - 설명
* BoT_BE/application is for android app transaction(get/post). This server can register users info and IoTs info. Also communicate with client to provide IoT's state.
* BoT_BE/appcication_tcp is for IoT transaction. This server can get some tcp transaction from IoT.
* BoT_BE/application_view is for web client user. This server provide blockchain transaction info for web clients.

## blueprint - 설계도
* Stystem
![image](https://user-images.githubusercontent.com/58164975/131520877-0388357c-8a88-4124-ba7a-ed33d1068413.png)

* Hyperledger Fabric Network
![1](https://user-images.githubusercontent.com/58164975/148058062-e926ec75-18d8-45fb-80a5-bbe373526391.jpg)

## How To Run(실행 방법)
## 1.Node.js module install
* Go to BoT_BE/application/ and BoT_BE/application_view/ 
```
npm install
```

## 2.Starting the Fabric Network(start network, create channel and join, chaincode install and isntantiate)
* Go to BoT_BE/basic-network/
```
./generate.sh
../startBot.sh
```

## 3.Enroll org1's 'admin' user and 'user1' user(Certification)
* Go to BoT_BE/application
```
./node enrollAdmin.js
./node registerUser.js
```

## 4.application Server start
```
./node app.js
```
* Now server listening on localhost:3000 for android clients.

## 5.application_tcp Server start
* Go to BoT_BE/application_tcp
```
./node tcp_server.js
```
* Now server listening on localhost:3001 for IoTs.

## 6.Enroll org2's 'admin' user and 'user1' user(Certification)
* Go to BoT_BE/application_view
```
./node enrollAdmin.js
./node registerUser.js
```

## 7.application_view Server start
* Server start
```
./npm start
```
* Now server listening on localhost:3002 for web clients.
