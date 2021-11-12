MATCH (n:Person) WHERE n.born IS NOT NULL
WITH n ORDER BY n.born 
WITH example.last(n) as last
RETURN last, last.born