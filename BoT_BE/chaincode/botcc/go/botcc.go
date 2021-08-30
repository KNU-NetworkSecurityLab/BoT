// package name
package main

 // module import
import (
	"bytes"
	"fmt"
	"strconv"
	"encoding/json"
 	"time"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	"github.com/hyperledger/fabric/protos/peer"
 )
 
 // class, struct define
type SmartContract struct {
 }

type User struct {
	Key		string `json:"key"`
	Id		string `json:"id"`
	Pw		string `json:"pw"`
	Name	string `json:"name"`
	Mail	string `json:"mail"`
	Phone	string `json:"phone"`
	Team	string `json:"team"`
} 

type UserKey struct {
	Key string
	Idx int
}

type IoT struct {
	Devicekey	string `json:"devicekey"`
	Device		string `json:"device"`
	UserId		string `json:"userid"`
	Date		string `json:"date"`
	Feedback	string `json:"feedback"`
	Ip			string `json:ip`
	Area		string `json:"area"`
	Gas			float64 `json:"gas"`
	Tmp			float64 `json:"tmp"`
	Hum			float64 `json:"hum"`
	Lamp		bool `json:"lamp"`
} 

type IotKey struct {
	Devicekey string
	Idx int
}

 // Init function
func (t *SmartContract) Init(stub shim.ChaincodeStubInterface) peer.Response {
	return shim.Success(nil)
}

 // Invoke function
func (t *SmartContract) Invoke(stub shim.ChaincodeStubInterface) peer.Response {

	fn, args := stub.GetFunctionAndParameters()

	if fn == "addUser" {
		return t.addUser(stub, args)
	} else if fn == "login" {
		return t.login(stub, args)
	} else if fn == "queryAllUsers" {
		return t.queryAllUsers(stub)
	} else if fn == "getUserInfo" {
		return t.getUserInfo(stub, args)
	} else if fn == "addIot" {
		return t.addIot(stub, args)
	} else if fn == "updateIot" {
		return t.updateIot(stub, args)
	} else if fn == "lampStateUpdate" {
		return t.lampStateUpdate(stub, args)
	} else if fn == "gasStateUpdate" {
		return t.gasStateUpdate(stub, args)
	} else if fn == "tmpHumStateUpdate" {
		return t.tmpHumStateUpdate(stub, args)
	} else if fn == "iotFind" {
		return t.iotFind(stub, args)
	} else if fn == "iotFindByArea" {
		return t.iotFindByArea(stub, args)
	} else if fn == "queryAllIoTs" {
		return t.queryAllIoTs(stub)
	} else if fn == "getHistory" {
		return t.getHistory(stub, args)
	}
	
	return shim.Error("Invalid Smart Contract function name.")
}


/* ------------------------- USER functions ------------------------- */

// Add User
 func (t *SmartContract) addUser(stub shim.ChaincodeStubInterface, args []string) peer.Response {
	if len(args) != 6 {
		return shim.Error("incorrectArgumentsExpecting6")
	}

	// checking duplication userId
	_, isExist := userFindFromId(stub, args, "addUser")
	if isExist == true {
		return shim.Error("alreadyExistingId")
	}

	var userkey = UserKey{}
	json.Unmarshal(userKeyGenerate(stub), &userkey)
	keyIdx := strconv.Itoa(userkey.Idx)

	var keyString = userkey.Key + keyIdx
	var user = User{Key: keyString, Id: args[0], Pw: args[1], Name: args[2], Mail: args[3], Phone: args[4], Team: args[5]}
	userAsBytes, _ := json.Marshal(user)
	
	err := stub.PutState(keyString, userAsBytes)
	if err != nil {
		return shim.Error("failedToRecordUser")
	}
	userKeyAsBytes, _ := json.Marshal(userkey)
	stub.PutState("latestUserKey", userKeyAsBytes)

	return shim.Success(nil)
}

// Login
func (t *SmartContract) login(stub shim.ChaincodeStubInterface, args []string) peer.Response {
	if(len(args) != 2) {
		return shim.Error("incorrectArgumentsExpecting2")
	}


	user, isExist := userFindFromId(stub, args, "login")
	if isExist == false {
		return shim.Error("incorrectId")
	}
	if user.Pw != args[1] {
		return shim.Error("incorrectPw")
	}

	return shim.Success(nil)
}

// Query All Users Info
func (t *SmartContract) queryAllUsers(stub shim.ChaincodeStubInterface) peer.Response {
	userKeyAsBytes, _ := stub.GetState("latestUserKey")
	userkey := UserKey{}
	json.Unmarshal(userKeyAsBytes, &userkey)
	idxStr := strconv.Itoa(userkey.Idx + 1)

	var startKey = "USER0"
	var endKey = userkey.Key + idxStr

	resultIter, err := stub.GetStateByRange(startKey, endKey)
	if err != nil {
		return shim.Error("getStateByRangeError")
	}
	defer resultIter.Close()

	var buffer bytes.Buffer
	buffer.WriteString("[")
	bArrayMemberAlreadyWritten := false

	for resultIter.HasNext() {
		queryResponse, err := resultIter.Next()
		if err != nil {
			return shim.Error("resultiterError")
		}
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}
		buffer.WriteString("{\"Key\":")
		buffer.WriteString("\"")
		buffer.WriteString(queryResponse.Key)
		buffer.WriteString("\"")
	
		// Record is a JSON object, so we write as-is
		buffer.WriteString(", \"Record\":")		
		buffer.WriteString(string(queryResponse.Value))
		buffer.WriteString("}")
		bArrayMemberAlreadyWritten = true
	}

	buffer.WriteString("]")
	return shim.Success(buffer.Bytes())
}

// Get One User's Info
func (t *SmartContract) getUserInfo(stub shim.ChaincodeStubInterface, args []string) peer.Response {
	if(len(args) != 1) {
		return shim.Error("incorrectArgumentsExpecting1")
	}

	user, isExist := userFindFromId(stub, args, "login")
	if isExist == false {
		return shim.Error("infoNotExist")
	}

	userAsBytes, _ := json.Marshal(user)
	return shim.Success(userAsBytes)
}

// Find User's Id (For Checking Login or AddUser)
func userFindFromId(stub shim.ChaincodeStubInterface, args []string, fns string) (User, bool) {
	userKeyAsBytes, _ := stub.GetState("latestUserKey")
	userkey := UserKey{}
	json.Unmarshal(userKeyAsBytes, &userkey)
	idxStr := strconv.Itoa(userkey.Idx + 1)

	var startKey = "USER0"
	var endKey = userkey.Key + idxStr

	resultIter, err := stub.GetStateByRange(startKey, endKey)
	if err != nil {
		fmt.Println(err.Error())
	}
	defer resultIter.Close()

	for resultIter.HasNext() {
		queryResponse, err := resultIter.Next()
		if err != nil {
			fmt.Println(err.Error())
		}
		
		user := User{}
		json.Unmarshal(queryResponse.Value, &user)

		if user.Id == args[0] {
			if fns == "login" {
				return user, true;
			} else if fns == "addUser" {
				return User{}, true
			}				
		}		
	}

	return User{}, false
}

// Generate User's Key function
func userKeyGenerate(stub shim.ChaincodeStubInterface) []byte {
	var isFirst bool = false

	userKeyAsBytes, err := stub.GetState("latestUserKey")
	if err != nil {
		fmt.Println(err.Error())
	}

	userkey := UserKey{}
	json.Unmarshal(userKeyAsBytes, &userkey)
	var tempIdx string
	tempIdx = strconv.Itoa(userkey.Idx)
	if len(userkey.Key) == 0 || userkey.Key == "" {
		isFirst = true
		userkey.Key = "USER"
	}
	if !isFirst {
		userkey.Idx = userkey.Idx + 1
	}
	fmt.Println("Last UserKey is " + userkey.Key + " : " + tempIdx)

	returnUserBytes, _ := json.Marshal(userkey)
	return returnUserBytes	
}


/* ------------------------- IoT functions ------------------------- */
// Add IoT Info
func (t *SmartContract) addIot(stub shim.ChaincodeStubInterface, args []string) peer.Response {
	if len(args) != 9 {
		return shim.Error("incorrectArgumentsExpecting9")
	}

	var iotkey = IotKey{}
	json.Unmarshal(iotKeyGenerate(stub), &iotkey)
	keyIdx := strconv.Itoa(iotkey.Idx)

	var lamp bool = false
	var gas float64 = -1
	var tmp float64 = -1
	var hum float64 = -1
	var err error

	if args[3] != "" {
		lamp, err = strconv.ParseBool(args[3])
	} 
	
	if args[4] != "" {
		gas, err = strconv.ParseFloat(args[4], 64)
	} 
	
	if args[5] != ""  {
		tmp, err = strconv.ParseFloat(args[5], 64)
		hum, err = strconv.ParseFloat(args[6], 64)
	}

	if err != nil {
		return shim.Error("failedRecordIoTCatch")
	}

	// Setting to Korea Time
	loc, err := time.LoadLocation("Asia/Seoul")
	if err != nil {
		return shim.Error("failedTimeLocate")
	}

	now := time.Now().In(loc)
	custom := now.Format("2006-01-02 15:04:05")
	var keyString = iotkey.Devicekey + keyIdx
	var iot = IoT{Devicekey: keyString, Device: args[0], UserId: args[1], Area: args[2], Lamp: lamp, Gas: gas, Tmp: tmp, Hum: hum, Date: custom, Feedback: args[7], Ip: args[8]}
	iotAsBytes, _ := json.Marshal(iot)
	
	
	err = stub.PutState(keyString, iotAsBytes)
	if err != nil {
		return shim.Error("failedRecordIoTCatch")
	}
	iotKeyAsBytes, _ := json.Marshal(iotkey)
	stub.PutState("latestIotKey", iotKeyAsBytes)

	return shim.Success(nil)
}

// Update IoT Info
func (t *SmartContract) updateIot(stub shim.ChaincodeStubInterface, args []string) peer.Response {
	if len(args) != 10 {
		return shim.Error("incorrectArgumentsExpecting10")
	}

	var lamp bool = false
	var gas float64 = -1
	var tmp float64 = -1
	var hum float64 = -1
	var err error

	if args[4] != "" {
		lamp, err = strconv.ParseBool(args[4])
	} 
	
	if args[5] != "" {
		gas, err = strconv.ParseFloat(args[5], 64)
	} 
	
	if args[6] != ""  {
		tmp, err = strconv.ParseFloat(args[6], 64)
		hum, err = strconv.ParseFloat(args[7], 64)
	}

	if err != nil {
		return shim.Error("failedRecordIoTCatch")
	}

	// Setting to Korea Time
	loc, err := time.LoadLocation("Asia/Seoul")
	if err != nil {
		return shim.Error("failedTimeLocate")
	}

	now := time.Now().In(loc)
	custom := now.Format("2006-01-02 15:04:05")
	var iot = IoT{Devicekey: args[0], Device: args[1], UserId: args[2], Area: args[3], Lamp: lamp, Gas: gas, Tmp: tmp, Hum: hum, Date: custom, Feedback: args[8], Ip: args[9]}
	iotAsBytes, _ := json.Marshal(iot)
	
	
	err = stub.PutState(args[0], iotAsBytes)
	if err != nil {
		return shim.Error("failedRecordIoTCatch")
	}

	return shim.Success(nil)
}

// Lamp State Update (on, off)
func (t *SmartContract) lampStateUpdate(stub shim.ChaincodeStubInterface, args[] string) peer.Response {
	if len(args) != 2 {
		return shim.Error("incorrectArgumentsExpecting2")
	}

	iotAsBytes, _ := stub.GetState(args[0])
	if iotAsBytes == nil {
		return shim.Error("couldNotLocateIoT")
	}

	// Setting to Korea Time
	loc, err := time.LoadLocation("Asia/Seoul")
	if err != nil {
		return shim.Error("failedTimeLocate")
	}
	
	now := time.Now().In(loc)
	custom := now.Format("2006-01-02 15:04:05")		

	iot := IoT{}
	json.Unmarshal(iotAsBytes, &iot)
	iot.Lamp, _ = strconv.ParseBool(args[1])
	iot.Date = custom
	iotAsBytes, _ = json.Marshal(iot)
	err = stub.PutState(args[0], iotAsBytes)
	if err != nil {
		return shim.Error("failedToChangeIoTholder")
	}
	return shim.Success(iotAsBytes)

}

// Gas State Update (0.0, 1.4...)
func (t *SmartContract) gasStateUpdate(stub shim.ChaincodeStubInterface, args[] string) peer.Response {
	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}
	iotAsBytes, _ := stub.GetState(args[0])
	if iotAsBytes == nil {
		return shim.Error("Could not locate IoT")
	}

	// Setting to Korea Time
	loc, err := time.LoadLocation("Asia/Seoul")
	if err != nil {
		return shim.Error(fmt.Sprintf("Failed to time locate"))
	}
	
	now := time.Now().In(loc)
	custom := now.Format("2006-01-02 15:04:05")		
	
	iot := IoT{}
	json.Unmarshal(iotAsBytes, &iot)
	iot.Gas, _ = strconv.ParseFloat(args[1], 64)
	iot.Date = custom
	iotAsBytes, _ = json.Marshal(iot)
	err = stub.PutState(args[0], iotAsBytes)
	if err != nil {
		return shim.Error(fmt.Sprintf("Failed to change IoT holder: %s", args[0]))
	}
	return shim.Success(nil)

}

// Temperature and Humidity State Update
func (t *SmartContract) tmpHumStateUpdate(stub shim.ChaincodeStubInterface, args[] string) peer.Response {
	if len(args) != 3 {
		return shim.Error("Incorrect number of arguments. Expecting 3")
	}
	iotAsBytes, _ := stub.GetState(args[0])
	if iotAsBytes == nil {
		return shim.Error("Could not locate IoT")
	}
	
	// Setting to Korea Time
	loc, err := time.LoadLocation("Asia/Seoul")
	if err != nil {
		return shim.Error(fmt.Sprintf("Failed to time locate"))
	}
	
	now := time.Now().In(loc)
	custom := now.Format("2006-01-02 15:04:05")		

	iot := IoT{}
	json.Unmarshal(iotAsBytes, &iot)
	iot.Tmp, _ = strconv.ParseFloat(args[1], 64)
	iot.Hum, _ = strconv.ParseFloat(args[2], 64)
	iot.Date = custom
	iotAsBytes, _ = json.Marshal(iot)
	err = stub.PutState(args[0], iotAsBytes)
	if err != nil {
		return shim.Error(fmt.Sprintf("Failed to change IoT holder: %s", args[0]))
	}
	return shim.Success(nil)

}

// Find Specific User's IoT Info From UserId Include IoT State
func (t *SmartContract) iotFind(stub shim.ChaincodeStubInterface, args []string) peer.Response {
	if len(args) != 1 {
		return shim.Error("incorrectArgumentsExpecting1")
	}

	iotKeyAsBytes, _ := stub.GetState("latestIotKey")
	iotkey := IotKey{}
	json.Unmarshal(iotKeyAsBytes, &iotkey)
	idxStr := strconv.Itoa(iotkey.Idx + 1)

	var startKey = "IoT0"
	var endKey = iotkey.Devicekey + idxStr

	resultIter, err := stub.GetStateByRange(startKey, endKey)
	if err != nil {
		fmt.Println(err.Error())
	}
	defer resultIter.Close()

	var buffer bytes.Buffer
	buffer.WriteString("[")
	bArrayMemberAlreadyWritten := false

	for resultIter.HasNext() {
		queryResponse, err := resultIter.Next()
		if err != nil {
			fmt.Println(err.Error())
		}
		iot := IoT{}
		json.Unmarshal(queryResponse.Value, &iot)

		if iot.UserId == args[0] {
			if bArrayMemberAlreadyWritten == true {
				buffer.WriteString(",")
			}
			buffer.WriteString("{\"Key\":")
			buffer.WriteString("\"")
			buffer.WriteString(queryResponse.Key)
			buffer.WriteString("\"")
		
			// Record is a JSON object, so we write as-is
			buffer.WriteString(", \"Record\":")		
			buffer.WriteString(string(queryResponse.Value))
			buffer.WriteString("}")
			bArrayMemberAlreadyWritten = true
	
		}		
	}

	buffer.WriteString("]")
	if buffer.Len() == 2 {
		return shim.Error("notExistUserid")
	}
	return shim.Success(buffer.Bytes())
}


// Find Specific User's IoT Info From UserId Include IoT State
func (t *SmartContract) iotFindByArea(stub shim.ChaincodeStubInterface, args []string) peer.Response {
	if len(args) != 2 {
		return shim.Error("incorrectArgumentsExpecting2")
	}

	iotKeyAsBytes, _ := stub.GetState("latestIotKey")
	iotkey := IotKey{}
	json.Unmarshal(iotKeyAsBytes, &iotkey)
	idxStr := strconv.Itoa(iotkey.Idx + 1)

	var startKey = "IoT0"
	var endKey = iotkey.Devicekey + idxStr

	resultIter, err := stub.GetStateByRange(startKey, endKey)
	if err != nil {
		fmt.Println(err.Error())
	}
	defer resultIter.Close()

	var buffer bytes.Buffer
	buffer.WriteString("[")
	bArrayMemberAlreadyWritten := false

	for resultIter.HasNext() {
		queryResponse, err := resultIter.Next()
		if err != nil {
			fmt.Println(err.Error())
		}
		iot := IoT{}
		json.Unmarshal(queryResponse.Value, &iot)

		if iot.UserId == args[0] {
			if iot.Area == args[1] {
				if bArrayMemberAlreadyWritten == true {
					buffer.WriteString(",")
				}
				buffer.WriteString("{\"Key\":")
				buffer.WriteString("\"")
				buffer.WriteString(queryResponse.Key)
				buffer.WriteString("\"")
			
				// Record is a JSON object, so we write as-is
				buffer.WriteString(", \"Record\":")		
				buffer.WriteString(string(queryResponse.Value))
				buffer.WriteString("}")
				bArrayMemberAlreadyWritten = true
			}
	
		}		
	}

	buffer.WriteString("]")
	if buffer.Len() == 2 {
		return shim.Error("notExistUseridOrArea")
	}
	return shim.Success(buffer.Bytes())
}


// Show All State of IoT
func (t *SmartContract) queryAllIoTs(stub shim.ChaincodeStubInterface) peer.Response {
	iotKeyAsBytes, _ := stub.GetState("latestIotKey")
	iotkey := IotKey{}
	json.Unmarshal(iotKeyAsBytes, &iotkey)
	idxStr := strconv.Itoa(iotkey.Idx + 1)

	var startKey = "IoT0"
	var endKey = iotkey.Devicekey + idxStr

	resultIter, err := stub.GetStateByRange(startKey, endKey)
	if err != nil {
		return shim.Error("getStateByRangeError")
	}
	defer resultIter.Close()

	var buffer bytes.Buffer
	buffer.WriteString("[")
	bArrayMemberAlreadyWritten := false

	for resultIter.HasNext() {
		queryResponse, err := resultIter.Next()
		if err != nil {
			return shim.Error("resultIterError")
		}
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}
		buffer.WriteString("{\"Key\":")
		buffer.WriteString("\"")
		buffer.WriteString(queryResponse.Key)
		buffer.WriteString("\"")
	
		// Record is a JSON object, so we write as-is
		buffer.WriteString(", \"Record\":")		
		buffer.WriteString(string(queryResponse.Value))
		buffer.WriteString("}")
		bArrayMemberAlreadyWritten = true
	}

	buffer.WriteString("]")
	return shim.Success(buffer.Bytes())
}


//IotKey generateKey
func iotKeyGenerate(stub shim.ChaincodeStubInterface) []byte {
	var isFirst bool = false

	iotKeyAsBytes, err := stub.GetState("latestIotKey")
	if err != nil {
		fmt.Println(err.Error())
	}

	iotkey := IotKey{}
	json.Unmarshal(iotKeyAsBytes, &iotkey)
	var tempIdx string
	tempIdx = strconv.Itoa(iotkey.Idx)
	if len(iotkey.Devicekey) == 0 || iotkey.Devicekey == "" {
		isFirst = true
		iotkey.Devicekey = "IoT"
	}
	if !isFirst {
		iotkey.Idx = iotkey.Idx + 1
	}
	fmt.Println("Last IoTKey is " + iotkey.Devicekey + " : " + tempIdx)

	returnIotBytes, _ := json.Marshal(iotkey)
	return returnIotBytes	
}

 // main function
func main() {	
	err := shim.Start(new(SmartContract))
	if err != nil {
		fmt.Println("Error creating new Smart Cont9ract : %s", err)
	}
}


// Get Transaction's History
func (t *SmartContract) getHistory(stub shim.ChaincodeStubInterface, args []string) peer.Response {

	if len(args) < 1 {
	   return shim.Error("incorrectArgumentsExpecting1")
	}
	keyName := args[0]
	// 로그 남기기
	fmt.Println("getHistory:" + keyName)
 
	resultsIterator, err := stub.GetHistoryForKey(keyName)
	if err != nil {
	   return shim.Error("resultiterError")
	}
	defer resultsIterator.Close()
 
	// Setting to Korea Time
	loc, err := time.LoadLocation("Asia/Seoul")
	if err != nil {
		return shim.Error("failedTimeLocate")
	}

	// buffer is a JSON array containing historic values for the marble
	var buffer bytes.Buffer
	buffer.WriteString("[")
 
	bArrayMemberAlreadyWritten := false
	for resultsIterator.HasNext() {
	   response, err := resultsIterator.Next()
	   if err != nil {
		  return shim.Error("resultiterError")
	   }
	   if bArrayMemberAlreadyWritten == true {
		  buffer.WriteString(",")
	   }
	   buffer.WriteString("{\"TxId\":")
	   buffer.WriteString("\"")
	   buffer.WriteString(response.TxId)
	   buffer.WriteString("\"")
 
	   buffer.WriteString(", \"Value\":")
	   if response.IsDelete {
		  buffer.WriteString("null")
	   } else {
		  buffer.WriteString(string(response.Value))
	   }

	   now := time.Unix(response.Timestamp.Seconds, int64(response.Timestamp.Nanos)).In(loc)
	   custom := now.Format("2006-01-02 15:04:05")

	   buffer.WriteString(", \"Timestamp\":")
	   buffer.WriteString("\"")
	   buffer.WriteString(custom)


	   buffer.WriteString("\"")
 
	   buffer.WriteString(", \"IsDelete\":")
	   buffer.WriteString("\"")
	   buffer.WriteString(strconv.FormatBool(response.IsDelete))
	   buffer.WriteString("\"")
 
	   buffer.WriteString("}")
	   bArrayMemberAlreadyWritten = true
	}
	buffer.WriteString("]")
 
	// 로그 남기기
	fmt.Println("getHistory returning:\n" + buffer.String() + "\n")
 
	return shim.Success(buffer.Bytes())
 }