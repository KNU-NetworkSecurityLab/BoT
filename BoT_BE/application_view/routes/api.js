'use strict';

const express = require('express');
const router = express.Router();

const { FileSystemWallet, Gateway } = require('fabric-network');
const fs = require('fs');
const path = require('path');

const ccpPath = path.resolve(__dirname, '..', '..', 'basic-network', 'connection-org2.json');
const ccpJSON = fs.readFileSync(ccpPath, 'utf8');
const ccp = JSON.parse(ccpJSON);

/* GET users listing. */
router.get('/', function(req, res, next) {
  res.send('respond with a resource');
});

router.get('/queryAllIoTs', async (req, res) => {
  try {
    const result = await callChainCode('queryAllIoTs', false);

    if (result == 'getStateByRangeError') {
      res.status(400).send('queryAllIoTs_Failed');
    } else if (result == 'resultIterError') {
      res.status(400).send('queryAllIoTs_Failed');
    } else if (result == 'walletError') {
      res.status(400).send('queryAllIoTs_Failed_walletError');
    } else {
      res.json(JSON.parse(result));
    }
  } catch(err) {
    console.log(err);
    res.status(400).send('queryAllIoTs_Failed');
  }
});

router.get('/queryAllUsers', async (req, res) => {
  try {
    const result = await callChainCode('queryAllUsers', false);
    if (result == 'getStateByRangeError') {
      res.status(400).send('queryAllUsers_Failed');
    } else if (result == 'resultiterError') {
      res.status(400).send('queryAllUsers_Failed');
    } else if (result == 'walletError') {
      res.status(400).send('queryAllUsers_Failed_walletError');
    } else {
      //res.status(200).json(JSON.parse(result));
      res.send(JSON.parse(result))
    }    
  } catch(err) {
    console.log(err);
    res.status(400).send('queryAllUsers_Failed');
  }
});

router.get('/getHistory/:iotNo', async (req, res) => {
  const iotNo = req.params.iotNo;
  try {
    const result = await callChainCode('getHistory', false, iotNo);

    if (result == 'incorrectArgumentsExpecting1') {
      res.status(400).send('getHistory_Failed_incorrectArgumentsExpecting1');
    } else if (result == 'resultiterError') {
      res.status(400).send('getHistory_Failed');
    } else if (result == 'failedTimeLocate') {
      res.status(200).send('getHistory_Failed');
    } else if (result == 'walletError') {
      res.status(400).send('getHistory_Failed_walletError');
    } else {
      res.json(JSON.parse(result));
    }    
  } catch(err) {
    console.log(err);
    res.status(400).send('getHistory_Failed');
  }
});


// Call Chaincode
async function callChainCode(fnName, isSubmit, ...args) {
  try {
    // Create a new file system based wallet for managing identities.
    const walletPath = path.join(process.cwd(), 'wallet');
    const wallet = new FileSystemWallet(walletPath);
    console.log(`Wallet path: ${walletPath}`);

    // Check to see if we've already enrolled the user.
    const userExists = await wallet.exists('user1');
    if (!userExists) {
        console.log('An identity for the user "user1" does not exist in the wallet');
        console.log('Run the registerUser.js application before retrying');
        return 'walletError';
    }


    // Create a new gateway for connecting to our peer node.
    const gateway = new Gateway();
    await gateway.connect(ccp, { wallet, identity: 'user1', discovery: { enabled: false } });

    // Get the network (channel) our contract is deployed to.
    const network = await gateway.getNetwork('mychannel');

    // Get the contract from the network.
    const contract = network.getContract('botcc');

    let result;
    if(isSubmit) {
      result = await contract.submitTransaction(fnName, ...args);
      console.log('Transaction has been submitted.');
    } else {
      result = await contract.evaluateTransaction(fnName, ...args);
      console.log(`Transaction has been evaluated. result: ${result.toString()}`);
    }
    return result;

    // Chaincoe Error
  } catch(err) {
    //console.error(`Failed to create transaction: ${err}`);
    if (err.message.indexOf('message=') == -1) {
      return err.message
    } else {
      var index = err.message.split('message=')
      var index2 = index[1].split(' ')
      return index2[0];
    }
  }
}

module.exports = router;
