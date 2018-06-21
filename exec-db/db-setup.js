use analyticsdb;

db.createUser({
  user: "analyticsuser",
  pwd: "analyticspass",
  "roles": [
    {
      "role": "readWrite",
      "db": "analyticsdb"
    }
  ]
});