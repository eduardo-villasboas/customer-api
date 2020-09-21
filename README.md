
sudo docker run --name db-customer-api -e POSTGRES_PASSWORD=customer-password -e POSTGRES_USER=customer-user -e POSTGRES_DB=customer-local -p 5431:5432 --restart=unless-stopped -d postgres:10
