db.createUser({
    user: "adminUser",
    pwd: "admin_password",
    roles: [
        { role: "userAdminAnyDatabase", db: "admin" },
        { role: "readWrite", db: "bus-tracking" }
    ]
});

db.createUser({
    user: "serviceUser",
    pwd: "service_password",
    roles: [
        { role: "readWrite", db: "bus-tracking" }
    ]
});
