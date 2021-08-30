#!/bin/bash
#
# Copyright IBM Corp All Rights Reserved
#
# SPDX-License-Identifier: Apache-2.0
#
# Exit on first error
set -e

# don't rewrite paths for Windows Git Bash users
export MSYS_NO_PATHCONV=1
starttime=$(date +%s)
CC_SRC_LANGUAGE=${1:-"go"}
CC_SRC_LANGUAGE=`echo "$CC_SRC_LANGUAGE" | tr [:upper:] [:lower:]`
if [ "$CC_SRC_LANGUAGE" = "go" -o "$CC_SRC_LANGUAGE" = "golang"  ]; then
	CC_RUNTIME_LANGUAGE=golang
  CC_SRC_PATH=github.com/botcc/go

else
	echo The chaincode language ${CC_SRC_LANGUAGE} is not supported by this script
	echo Supported chaincode languages are: go, javascript, and typescript
	exit 1
fi

# clean the keystore
rm -rf ./application/hfc-key-store
rm -rf ./application_view/hfc-key-store
# 3. 네트워크 재실행 시 key file이 들어있는 wallet directory 삭제
rm -rf ./application/wallet/*
rm -rf ./application_view/wallet/*

# launch network; create channel and join peer to channel
cd ./basic-network
./start.sh

# Now launch the CLI container in order to install, instantiate chaincode
# and prime the ledger with our 10 cars
docker-compose -f ./docker-compose.yml up -d cli
docker ps -a

# 3. 배포할 체인코드 이름 변경
# Chaincode org1 install
docker exec -e "CORE_PEER_LOCALMSPID=Org1MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.bot.com/users/Admin@org1.bot.com/msp" cli peer chaincode install -n botcc -v 1.0 -p "$CC_SRC_PATH" -l "$CC_RUNTIME_LANGUAGE"

# Chaincode instantiate
docker exec -e "CORE_PEER_LOCALMSPID=Org1MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.bot.com/users/Admin@org1.bot.com/msp" cli peer chaincode instantiate -o orderer.bot.com:7050 -C mychannel -n botcc -l "$CC_RUNTIME_LANGUAGE" -v 1.0 -c '{"Args":[]}' -P "OR ('Org1MSP.member','Org2MSP.member')"
sleep 5



cat <<EOF

Total setup execution time : $(($(date +%s) - starttime)) secs ...

EOF
