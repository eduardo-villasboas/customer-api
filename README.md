
### Este projeto demonstra a criação de uma api CRUD para clientes. 

---

#### Tecnologias, bibliotecas e técnicas.
- Java 11
- Gradle
- Springboot
- Jpa
- Junit5
- Mockito
- docker
- JsonPatch
- liquibase(a base de dados é criada automaticamente)
- Ports and adapters/clean architecture.

#### Para rodar testes pela linha de commando utilize o commando no linux.
```batch
./gradlew clean build test
```
#### Para rodar testes pela linha de commando utilize o commando no windows.
```batch
gradlew.bat clean build test
```
### Obs: Os testes mockMvc(controller), dependem de uma base de dados limpa para funcionar corretamenente. Os testes não deixam sujeira(dados) no banco.

</br>

#### Para rodar o banco local utilize o seguinte commando.
```bash
sudo docker run --name db-customer-api -e POSTGRES_PASSWORD=customer-password -e POSTGRES_USER=customer-user -e POSTGRES_DB=customer-local -p 5431:5432 --restart=unless-stopped -d postgres:10
```
### Obs: o banco esta sendo mapeado para a porta 5431. 

