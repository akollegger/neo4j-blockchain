MATCH (n:Person)
CALL example.getRelationshipTypes(n) YIELD outgoing, incoming 
RETURN outgoing, incoming