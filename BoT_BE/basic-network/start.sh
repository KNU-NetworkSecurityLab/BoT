#!/bin/bash
#
# Copyright IBM Corp All Rights Reserved
#
# SPDX-License-Identifier: Apache-2.0
#
# Exit on first error, print all commands.

function replacePrivateKey() {
    echo "ca key file exchange"
    cp docker-compose-template.yml docker-compose.yml
    PRIV_KEY1=$(ls crypto-config/peerOrganizations/org1.bot.com/ca/ | grep _sk)
    sed -i "s/CA_PRIVATE_KEY1/${PRIV_KEY1}/g" docker-compose.yml
    PRIV_KEY2=$(ls crypto-config/peerOrganizations/org2.bot.com/ca/ | grep _sk)
    sed -i "s/CA_PRIVATE_KEY2/${PRIV_KEY2}/g" docker-compose.yml
}
function checkPrereqs() {
    # check config dir
    if [ ! -d "crypto-config" ]; then
        echo "crypto-config dir missing"
        exit 1
    fi
    # check crypto-config dir
     if [ ! -d "config" ]; then
        echo "config dir missing"
        exit 1
    fi
}

checkPrereqs
replacePrivateKey

set -ev

# don't rewrite paths for Windows Git Bash users
export MSYS_NO_PATHCONV=1

docker-compose -f docker-compose.yml down

docker-compose -f docker-compose.yml up -d ca.bot.com ca.view.com orderer.bot.com peer0.org1.bot.com peer1.org1.bot.com peer0.org2.bot.com peer1.org2.bot.com couchdb1 couchdb2 cli
docker ps -a

# wait for Hyperledger Fabric to start
# incase of errors when running later commands, issue export FABRIC_START_TIMEOUT=<larger number>
export FABRIC_START_TIMEOUT=10
#echo ${FABRIC_START_TIMEOUT}
sleep ${FABRIC_START_TIMEOUT}


# Join peer0.org1.bot.com to the channel.
#docker exec -e "CORE_PEER_LOCALMSPID=Org1MSP" -e "CORE_PEER_MSPCONFIGPATH=/etc/hyperledger/msp/users/Admin@org1.bot.com/msp" peer0.org1.bot.com peer channel join -b /etc/hyperledger/configtx/mychannel.block
#sleep 5


# Create the channel
docker exec cli peer channel create -o orderer.bot.com:7050 -c mychannel -f /etc/hyperledger/configtx/channel.tx
sleep 4
# Join peer0.org1.bot.com to the channel.
docker exec -e "CORE_PEER_LOCALMSPID=Org1MSP" -e "CORE_PEER_MSPCONFIGPATH=/etc/hyperledger/msp/users/Admin@org1.bot.com/msp" peer0.org1.bot.com peer channel join -b /etc/hyperledger/configtx/mychannel.block
sleep 4
# Update peer0.org1.bot.com to Channel's anchor peer
docker exec -e "CORE_PEER_LOCALMSPID=Org1MSP" -e "CORE_PEER_MSPCONFIGPATH=/etc/hyperledger/msp/users/Admin@org1.bot.com/msp" peer0.org1.bot.com peer channel update -o orderer.bot.com:7050 -c mychannel -f /etc/hyperledger/configtx/Org1MSPanchors.tx
sleep 4

# Join peer1.org1.bot.com to the channel.
docker exec -e "CORE_PEER_ADDRESS=peer1.org1.bot.com:7051" -e "CORE_PEER_MSPCONFIGPATH=/etc/hyperledger/msp/users/Admin@org1.bot.com/msp" peer1.org1.bot.com peer channel join -b /etc/hyperledger/configtx/mychannel.block
sleep 4

# Join peer0.org2.bot.com to the channel.
docker exec -e "CORE_PEER_LOCALMSPID=Org2MSP" -e "CORE_PEER_MSPCONFIGPATH=/etc/hyperledger/msp/users/Admin@org2.bot.com/msp" -e "CORE_PEER_ADDRESS=peer0.org2.bot.com:7051" peer0.org2.bot.com peer channel join -b /etc/hyperledger/configtx/mychannel.block
sleep 4
# Update peer0.org2.bot.com to Channel's anchor peer
docker exec -e "CORE_PEER_LOCALMSPID=Org2MSP" -e "CORE_PEER_MSPCONFIGPATH=/etc/hyperledger/msp/users/Admin@org2.bot.com/msp" -e "CORE_PEER_ADDRESS=peer0.org2.bot.com:7051" peer0.org2.bot.com peer channel update -o orderer.bot.com:7050 -c mychannel -f /etc/hyperledger/configtx/Org2MSPanchors.tx
sleep 4

# Join peer1.org2.bot.com to the channel.
docker exec -e "CORE_PEER_ADDRESS=peer1.org2.bot.com:7051" -e "CORE_PEER_MSPCONFIGPATH=/etc/hyperledger/msp/users/Admin@org2.bot.com/msp" peer1.org2.bot.com peer channel join -b /etc/hyperledger/configtx/mychannel.block
sleep 4

