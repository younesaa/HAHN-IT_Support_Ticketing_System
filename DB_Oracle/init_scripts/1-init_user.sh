#!/bin/bash
set -e

# Connect to the root container and switch to FREEPDB1
sqlplus -S sys/${ORACLE_PASSWORD} AS SYSDBA <<EOF
-- Switch to the pluggable database
ALTER SESSION SET CONTAINER = FREEPDB1;

-- Create the tablespace
CREATE TABLESPACE app_data
    DATAFILE '/opt/oracle/oradata/app_data01.dbf'
    SIZE 100M
    AUTOEXTEND ON NEXT 10M
    MAXSIZE UNLIMITED;

-- Create the application user
CREATE USER ${ORACLE_APP_USER} IDENTIFIED BY ${ORACLE_APP_USER_PASSWORD}
    DEFAULT TABLESPACE app_data
    TEMPORARY TABLESPACE temp
    QUOTA UNLIMITED ON app_data;

-- Grant privileges to the application user
GRANT CREATE SESSION, CREATE TABLE, ALTER ANY TABLE, DROP ANY TABLE, 
    SELECT ANY TABLE, INSERT ANY TABLE, UPDATE ANY TABLE, DELETE ANY TABLE 
    TO ${ORACLE_APP_USER};

EOF
