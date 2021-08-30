'use strict';

const express = require('express');
const router = express.Router();

const { FileSystemWallet, Gateway } = require('fabric-network');
const fs = require('fs');
const path = require('path');
var net = require('net');

const ccpPath = path.resolve(__dirname, '..', '..', 'basic-network', 'connection.json');
const ccpJSON = fs.readFileSync(ccpPath, 'utf8');
const ccp = JSON.parse(ccpJSON);

const port = 22485;
var host = "192.168.0.150";


/* GET users listing. */
router.get('/', async (req, res) => {
  res.send('respond with a resource');
});


router.post('/addIot', async (req, res) => {
  const args = [req.body.device, req.body.id, req.body.area, req.body.lamp, req.body.gas, req.body.tmp, req.body.hum, req.body.feedback, req.body.ip];
  try {
    const result = await callChainCode('addIot', true, ...args);
    if (result == 'incorrectArgumentsExpecting9') {
      res.status(200).send('addIot_Failed_incorrectArgumentsExpecting9');
    } else if (result == 'failedRecordIoTCatch') {
      res.status(200).send('addIot_Failed');
    } else if (result == 'failedTimeLocate') {
      res.status(200).send('addIot_Failed');
    } else if (result == 'walletError') {
      res.status(200).send('addIot_Failed_walletError');
    } else if (result == "") {
      res.status(200).send('addIot_Success');
    } else {
      res.status(200).send('addIot_Failed');
    }
  } catch(err) {
    console.log(err);
    res.status(200).send('addIot_Failed');
  }
});

router.post('/updateIot', async (req, res) => {
  const args = [req.body.devicekey, req.body.device, req.body.id, req.body.area, req.body.lamp, req.body.gas, req.body.tmp, req.body.hum, req.body.feedback, req.body.ip];
  try {
    const result = await callChainCode('updateIot', true, ...args);
    if (result == 'incorrectArgumentsExpecting10') {
      res.status(200).send('updateIot_Failed_incorrectArgumentsExpecting10');
    } else if (result == 'failedRecordIoTCatch') {
      res.status(200).send('updateIot_Failed_notExsitIoTNo');
    } else if (result == 'failedTimeLocate') {
      res.status(200).send('updateIot_Failed');
    } else if (result == 'walletError') {
      res.status(200).send('updateIot_Failed_walletError');
    } else if (result == "") {
      res.status(200).send('updateIot_Success');
    } else {
      res.status(200).send('updateIot_Failed');
    }
  } catch(err) {
    console.log(err);
    res.status(200).send('addIot_Failed');
  }
});

router.post('/lampStateUpdate', async (req, res) => {
  const args = [req.body.devicekey, req.body.lamp];
  try {
    const result = await callChainCode('lampStateUpdate', true, ...args);
    const obj = JSON.parse(result)

    var socket = net.connect({
      port: port,
      host: obj.Ip
    });
    
    socket.setEncoding('utf8');

    // Arduino connection
    socket.on('connect', () => {
      console.log("connected to server");
  
      if(req.body.lamp == 'true') {
        setTimeout( () => {
            socket.write('ON');
        }, 1000);
      } else if(req.body.lamp == 'false') {
        setTimeout( () => {
          socket.write('OFF');
        }, 1000);
      }      
      setTimeout( () => {
        socket.destroy()
      }, 2000);      
    });

    socket.on('data', (data) => {
      console.log(data);
    });

    socket.on('close', () => {
      console.log('close');
    });

    socket.on('error', (err) => {
      console.log('on error: ', err.code);
    });
    

    if (result == 'incorrectArgumentsExpecting2') {
      res.status(200).send('lampStateUpdate_Failed_incorrectArgumentsExpecting2');
    } else if (result == 'couldNotLocateIoT') {
      res.status(200).send('lampStateUpdate_Failed_couldNotLocateIoT');
    } else if (result == 'failedTimeLocate') {
      res.status(200).send('lampStateUpdate_Failed');
    } else if (result == 'failedToChangeIoTholder') {
      res.status(200).send('lampStateUpdate_Failed');
    } else if (result == 'walletError') {
      res.status(200).send('lampStateUpdate_Failed_walletError');
    } else {
      res.status(200).send('lampStateUpdate_Success');
    }
  } catch(err) {
    console.log(err);
    res.status(200).send('lampStateUpdate_Failed');
  }
});

router.post('/iotFind', async (req, res) => {
  const args = [req.body.userId];
  try {
    const result = await callChainCode('iotFind', false, ...args);
    if (result == 'incorrectArgumentsExpecting1') {
      res.status(200).send('iotFind_Failed_incorrectArgumentsExpecting1');
    } else if (result == 'notExistUserid') {
      res.status(200).send('iotFind_Failed_notExistUserid');
    } else if (result == 'walletError') {
      res.status(200).send('iotFind_Failed_walletError');
    } else {
      res.status(200).json(JSON.parse(result));
    }
  } catch(err) {
    console.log(err);
    res.status(200).send('iotFind_Failed');
  }
});


router.post('/iotFindByArea', async (req, res) => {
  const args = [req.body.userId, req.body.area];
  try {
    const result = await callChainCode('iotFindByArea', false, ...args);
    if (result == 'incorrectArgumentsExpecting1') {
      res.status(200).send('iotFindByArea_Failed_incorrectArgumentsExpecting1');
    } else if (result == 'notExistUseridOrArea') {
      res.status(200).send('iotFindByArea_Failed_notExistUseridOrArea');
    } else if (result == 'walletError') {
      res.status(200).send('iotFindByArea_Failed_walletError');
    } else {
      res.status(200).json(JSON.parse(result));
    }
  } catch(err) {
    console.log(err);
    res.status(200).send('iotFindByArea_Failed');
  }
});

router.post('/queryAllIoTs', async (req, res) => {
  try {
    const result = await callChainCode('queryAllIoTs', false);
    if (result == 'getStateByRangeError') {
      res.status(200).send('queryAllIoTs_Failed');
    } else if (result == 'resultIterError') {
      res.status(200).send('queryAllIoTs_Failed');
    } else if (result == 'walletError') {
      res.status(200).send('queryAllIoTs_Failed_walletError');
    } else {
      res.json(JSON.parse(result));
    }
  } catch(err) {
    console.log(err);
    res.status(200).send('queryAllIoTs_Failed');
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