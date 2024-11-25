# Define the base directory
BASE_DIR="/Users/alejandro/Developer/utl/arquitectura de software/utpolis"

sudo docker build -t pumpinator/utpolis-conf "$BASE_DIR"/api/conf
sudo docker build -t pumpinator/utpolis-eureka "$BASE_DIR"/api/eureka
sudo docker build -t pumpinator/utpolis-gateway "$BASE_DIR"/api/gateway
sudo docker build -t pumpinator/utpolis-usuario "$BASE_DIR"/api/microservicio/usuario
sudo docker build -t pumpinator/utpolis-persona "$BASE_DIR"/api/microservicio/persona
sudo docker build -t pumpinator/utpolis-venta "$BASE_DIR"/api/microservicio/venta
sudo docker build -t pumpinator/utpolis-ejemplo "$BASE_DIR"/api/microservicio/ejemplo
