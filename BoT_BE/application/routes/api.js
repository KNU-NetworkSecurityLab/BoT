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
  const args = [req.body.id, req.body.pw, req.body.name, req.body.mail, req.body.phone, req.body.team];
  try {
    const result = await callChainCode('addUser', true, ...args);
    if (result == 'error occurred!!!') {
      res.status(200).send('addUser_Failed');
    } else {
      res.status(200).send('addUser_Success');
    }
  } catch(err) {
    console.log(err);
    res.status(200).send('addUser_Failed');
  }
});

router.post('/login', async (req, res) => {
  const args = [req.body.id, req.body.pw];
  try {
    const result = await callChainCode('login', false, ...args);
    if (result == 'error occurred!!!') {
      res.status(200).send('login_Failed');
    } else {
      res.status(200).send('login_Success'); 
    }
    
  } catch(err) {
    console.log(err);
    res.status(200).send('login_Failed');
  }
});

router.get('/queryAllUsers', async (req, res) => {
  try {
    const result = await callChainCode('queryAllUsers', false);
    if (result == 'error occurred!!!') {
      res.status(200).send('queryAllUsers_Failed');
    } else {
      res.status(200).json(JSON.parse(result));
    }    
  } catch(err) {
    console.log(err);
    res.status(200).send('queryAllUsers_Failed');
  }
});

router.get('/getUser', async (req, res) => {
  const userKey = req.query.userkey;
  try {
    const result = await callChainCode('getUser', false, userKey);
    if (result == 'error occurred!!!') {
      res.status(200).send('getUser_Failed');
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
        return;
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

  } catch(err) {
    console.error(`Failed to create transaction: ${error}`);
    return 'error occurred!!!';
  }
}

module.exports = router;