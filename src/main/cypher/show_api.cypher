CALL dbms.functions() YIELD name, description
WHERE name STARTS WITH "example"
   OR name STARTS WITH "blockchain"
RETURN name, description, "function" as kind
UNION ALL
CALL dbms.procedures() YIELD name, description
WHERE name STARTS WITH "example"
   OR name STARTS WITH "blockchain"
RETURN name, description, "procedure" as kind