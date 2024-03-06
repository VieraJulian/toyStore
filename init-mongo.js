db = db.getSiblingDB('srv_orders');
db.createUser({
  user: 'root',
  pwd: 'password',
  roles: [{ role: 'readWrite', db: 'srv_orders' }],
});