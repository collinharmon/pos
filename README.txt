WhatIsThePointOfSales (WPS):

Welcome to the WPS package. All text in the readme is critical to running the supplied package, please be sure to read it prior to testing, as testing will fail without proper system understanding. 

**PLEASE VIEW USER MANUAL TO UNDERSTAND HOW TO USE SYSTEM

**PLEASE VIEW DEVELOPER MANUAL TO UNDERSTAND THE DESIGN OF THE SYSTEM


### NOTE ###
This is the third distributable provided. It demonstrates an advanced GUI implementation in order to showcase the full potential of the POS.  Please refer to the supplied testing document for testing information and procedures.

### Directories ###
/src - contains all the source .java files which were used for original compilation. There is no attached makefile to recompile the code, it is simply provided for link validation and diagram reference purposes.  

/dist - continues the executable and all required dependencies for the Construction phase, including but not limited to the oracle.jdbc driver used to integrate with the database.To run this software package please use the command java -jar pos.jar in the command line

/build - contains all the class files, as well as any relevant build information

/test - contains the systest class

### Classes Used ###

AddItem - swing class that accepts touch/keyboard I/O to retrieve item information

AddNewItem - swing class that accepts keyboard I/O to retrieve information relevant to adding a new item to the store database.

AdminFrame - swing class that is the façade for all all administrative functions

AdministrativeFunctions - system handler for all administrative functions, accepts signals form AdminFrame

Background - Background jFrame, does no work other than setting itself visible

CashorCredit - swing class that allows cashier to select payment type with touch/keyboard I/O. 

COS - Output-stream class that redirects all I/O t to the appropriate swing interfaces (primarily SysFrame).

ChangePassword - swing class that handles the accepting of user input for the updating of an employees password

EditEmployeeInfo - handles the updating of employee privilege in the database

Employee - façade class for all employee functions

Getccn - swing class that allows the casher to enter the credit card number required into the system.

InventoryLevels - swing class the creates an I/O frame for the outputting of inventory levels

Item - stores the data about items (id, name, quantity)

Login - swing class handles a simple login panel. (offline only)

LoginNew - swing class that handles a simple login panel (online only).

Money - custom money class that handles all casting/rounding needed to ensure accuracy in tax and cost information

NewReturn - handles the creation of a new return based on a RETTID

OAddItem - Adds an item to an offline sale handler

OItem - dumbed down version of item that handles offline transaction logging

ORemoveItem - removes an item from the offline cart

OSale - Offline handler for sales

OSys - Offline handler for sys

OWriter - File Writer for all pending SQL statements

OfflineFrame - swing class that mimics SysFrame for offline transactions. Unable to handle rental returns

PointofSale - driver class that implements all other functions (contains main method)

RefundCredit - Class handles refunds in cases where customer credit information is available

RemoveEmployeeInfo - swing class for retrieving the information to authenticate an employee for removal from the system.

RemoveItem - swing class for retrieving the information relevant to extracting an item from the cart

RemoveOldItem - swing class for removing items from the store database.

Return - façade for all return-related functions

ReturnItem - Flags an item as returned in the database

Sale - Sale class that contains a linked-list (sales line items) of all items in the sale. Used as primary connection to the database. Greatly trimmed down, and is used primarily as a local storage container.

SalesFrame - swing class responsible for displaying store sales data

SalesFunctions - façade class for all sales related functions 

Sys - Backend system class that handles the invocation of sales, as well as login and logout functions. Interface between front end swing classes and overarching back end POS system.

SysFrame - swing class that handles all I/O and input for user functions

TaxCalculator - Temporary tax-calculator which assumes national average tax information. This entity is designed to represent an interface with an external tax-calculator function. In future implementations (construction) it will be designed more intelligently to better represent the complexity of modern tax-management systems.

TenderCash - notification of amount of cash to tender

UpdatePrice - swing class that accepts item information used to authenticate an item for updating in the database

UpdateSuccessful - notification of success

VoidFrame - Transaction return handler [[[admin only]]]

### Run command (from current directory) ###
dist/java -jar pos.jar

### Working with the executable ###
This package interfaces with a database called edgar1. For this reason, we are using a username/PIN (password) system to connect with the database. 

For testing purposes please use the login :
username: sickmedic [[[admin frame]]]
password/pin: 0632
— or —
username: buschmaster [[[user frame]]]
password/pin: 1234

OFFLINE OVERRIDE: 0248

These will allow the package to connect with the database, without using local variables to store the actual username/password information. These are case-sensitive usernames.

We generated some basic testing data. To access the data please use the inputs provided and the following simple text matrix:

11111	Apple	1.99
11112	Pear	1.5
11113	Orange	1.45
11114	Milk	3.25
11115	Butter	2.78
11116	Guava	4.23
11117	Oats	7.38

			###	###IMPORTANT###	   ####

———Prior to testing and remove functions, please execute an add command!———


This data stored it in the database with the following table schema (generated using the following SQL):

  CREATE TABLE "CRB616"."SEEMPLOYEE" 
   (	"EID" VARCHAR2(5 BYTE) NOT NULL ENABLE, 
	"NAME" VARCHAR2(50 BYTE) NOT NULL ENABLE, 
	"UNAME" VARCHAR2(50 CHAR) NOT NULL ENABLE, 
	"PWORD" VARCHAR2(50 CHAR) NOT NULL ENABLE, 
	 PRIMARY KEY ("EID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
 

  CREATE TABLE "CRB616"."SEEMPLOYEE" 
   (	"EID" VARCHAR2(5 BYTE) NOT NULL ENABLE, 
	"NAME" VARCHAR2(50 BYTE) NOT NULL ENABLE, 
	"UNAME" VARCHAR2(50 CHAR) NOT NULL ENABLE, 
	"PWORD" VARCHAR2(50 CHAR) NOT NULL ENABLE, 
	 PRIMARY KEY ("EID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
 

  CREATE TABLE "CRB616"."SECONTAINS" 
   (	"PID" VARCHAR2(5 BYTE) NOT NULL ENABLE, 
	"TID" VARCHAR2(5 BYTE) NOT NULL ENABLE, 
	"QUANTITY" NUMBER(5,0) NOT NULL ENABLE, 
	"RETNAL" NUMBER(1,0) DEFAULT 0 NOT NULL ENABLE, 
	 PRIMARY KEY ("PID", "TID") DISABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
 

   COMMENT ON COLUMN "CRB616"."SECONTAINS"."RETNAL" IS 'Flags the item as a rental';
 

  CREATE OR REPLACE TRIGGER "CRB616"."SEINVENTORYUPDATE" after insert on SECONTAINS
FOR EACH ROW
BEGIN
    update SEINVENTORY set QUANTITY_AVALIABLE = QUANTITY_AVALIABLE - :NEW.QUANTITY
    where PID = :NEW.PID;
END;
/
ALTER TRIGGER "CRB616"."SEINVENTORYUPDATE" ENABLE;
 

  CREATE TABLE "CRB616"."SECONDUCTS" 
   (	"CID" VARCHAR2(5 BYTE) NOT NULL ENABLE, 
	"TID" VARCHAR2(5 BYTE) NOT NULL ENABLE, 
	 PRIMARY KEY ("CID", "TID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
 

  CREATE TABLE "CRB616"."SEINVENTORY" 
   (	"PID" VARCHAR2(5 BYTE), 
	"QUANTITY_AVALIABLE" NUMBER(10,2) NOT NULL ENABLE, 
	 PRIMARY KEY ("PID") DISABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
 

  CREATE TABLE "CRB616"."SEITEM" 
   (	"PID" VARCHAR2(5 BYTE) NOT NULL ENABLE, 
	"Name" VARCHAR2(50 BYTE) NOT NULL ENABLE, 
	"Price" NUMBER(10,2) NOT NULL ENABLE, 
	 PRIMARY KEY ("PID") DISABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
 

  CREATE TABLE "CRB616"."SERETTRANSACTION" 
   (	"RID" VARCHAR2(5 BYTE) NOT NULL ENABLE, 
	"RDATE" DATE DEFAULT SYSDATE, 
	 PRIMARY KEY ("RID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
 

  CREATE TABLE "CRB616"."SETRANSACTION" 
   (	"TID" VARCHAR2(5 BYTE) NOT NULL ENABLE, 
	"TDATE" DATE DEFAULT SYSDATE, 
	"SUBTOTAL" NUMBER(8,2) DEFAULT 0 NOT NULL ENABLE, 
	"TAX" NUMBER(10,2) DEFAULT 0 NOT NULL ENABLE, 
	"TOTAL" NUMBER(10,2) DEFAULT 0 NOT NULL ENABLE, 
	 PRIMARY KEY ("TID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
 
### IMPORTANT CREDIT CARD INFORMATION ###
The system required a valid credit card (i.e. a number that verifies the lhun algorithm). Please use a number greater than 10 digits or an actual credit card number to test.
