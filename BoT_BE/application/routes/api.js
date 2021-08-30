'use strict';

const express = require('express');
const router = express.Router();

const { FileSystemWallet, Gateway } = require('fabric-network');
const fs = require('fs');
const path = require('path');

const ccpPath = path.resolve(__dirname, '..', '..', 'basic-network', 'connection.json');
const ccpJSON = fs.readFileSync(ccpPath, 'utf8');
const ccp = JSON.parse(ccpJSON);

/* GET users listing. */
router.get('/', function(req, res) {
  res.send('respond with a resource');
});

router.post('/addUser', async (req, res) => {
  try {
    const args = [req.body.id, req.body.pw, req.body.name, req.body.mail, req.body.phone, req.body.team];
    const result = await callChainCode('addUser', true, ...args);

    if (result == 'incorrectArgumentsExpecting6') {
      res.status(200).send('addUser_Failed_incorrectArgumentsExpecting6');
    } else if (result == 'walletError') {
      res.status(200).send('addUser_Failed_walletError');
    } else if (result == 'alreadyExistingId') {
      res.status(200).send('addUser_Failed_alreadyExistingId');
    } else if (result == 'failedToRecordUser') {
      res.status(200).send('addUser_Failed');
    } else if (result == "") {
      res.status(200).send('addUser_Success');
    } else {
      res.status(200).send('addUser_Failed');
    }
  } catch(err) {
    console.log('this is err\n', err);
    res.status(200).send('addUser_Failed');
  }
});

router.post('/login', async (req, res) => {
  try {
    const args = [req.body.id, req.body.pw];
    const result = await callChainCode('login', false, ...args);

    console.log("result = ", result)

    if (result == 'incorrectArgumentsExpecting2') {
      res.status(200).send('login_Failed_incorrectArgumentsExpecting2');
    } else if (result == 'incorrectId') {
      res.status(200).send('login_Failed_incorrectId');
    } else if (result == 'incorrectPw') {
      res.status(200).send('login_Failed_incorrectPw');
    } else if (result == 'walletError') {
      res.status(200).send('login_Failed_walletError');
    } else if (result == ""){
      res.status(200).send('login_Success'); 
    } else {
      res.status(200).send('login_Failed');
    }
    
  } catch(err) {
    console.log(err);
    res.status(200).send('login_Failed');
  }
});

router.post('/queryAllUsers', async (req, res) => {
  try {
    const result = await callChainCode('queryAllUsers', false);
    if (result == 'getStateByRangeError') {
      res.status(200).send('queryAllUsers_Failed');
    } else if (result == 'resultiterError') {
      res.status(200).send('queryAllUsers_Failed');
    } else if (result == 'walletError') {
      res.status(200).send('queryAllUsers_Failed_walletError');
    } else {
      res.status(200).json(JSON.parse(result));
    }    
  } catch(err) {
    console.log(err);
    res.status(200).send('queryAllUsers_Failed');
  }
});

router.post('/getUserInfo', async (req, res) => {
  try {
    const args = [req.body.id];
    const result = await callChainCode('getUserInfo', false, ...args);
    if (result == 'incorrectArgumentsExpecting1') {
      res.status(200).send('getUser_Failed_incorrectArgumentsExpecting1');
    } else if (result == 'infoNotExist') {
      res.status(200).send('getUser_Failed_infoNotExist');
    }  else if (result == 'walletError') {
      res.status(200).send('getUser_Failed_walletError');
    } else {
      res.status(200).json(JSON.parse(result));
    }        
  } catch(err) {
    res.status(200).send('getUser_Failed');
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