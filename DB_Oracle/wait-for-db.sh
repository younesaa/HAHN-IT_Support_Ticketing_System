#!/bin/bash

# Wait for the PDB to be open
while true; do
    # Check if the PDB is open
    pdb_status=$(echo "SELECT status FROM v\$pdbs WHERE name = 'FREEPDB1';" | sqlplus -s ${ORACLE_APP_USER}/${ORACLE_APP_USER_PASSWORD}@//localhost:1521/FREEPDB1)

    # If the PDB is open, exit with success
    if [[ $pdb_status == *"OPEN"* ]]; then
        echo "FREEPDB1 is open."
        exit 0
    fi

    # If the PDB is not open, wait and retry
    echo "Waiting for FREEPDB1 to open..."
    sleep 10
done