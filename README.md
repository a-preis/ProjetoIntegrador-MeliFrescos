# ProjetoIntegrador - Mercado Livre: Frescos


Este Projeto Integrador se destina a simular uma nova modalidade de armazenamento, transporte e comercialização de produtos frescos, congelados e refrigerados do Mercado Livre através de uma API REST em Java com Spring Boot e suas respectivas dependências.

### Requisito 06: Registrar dimensões do produto

Permite o representante realizar o registro das dimensões de um produto já cadastrado, incluindo a altura, largura, comprimento em cm, e também o peso em gramas. Através dos endpoints criados, o representante pode fazer o CRUD das dimensões. Caso algum produto possua suas dimensões cadastradas, elas também serão exibidas nos endpoints de GET de produto.

### User Story:
[PDF](https://github.com/a-preis/ProjetoIntegrador-MeliFrescos/files/8632108/Requisito.06.docx.pdf)

### Endpoints Requisito 06:

![REQ06 ENDPOINTS](https://user-images.githubusercontent.com/101266447/166931944-fbe11db9-0119-445a-ad23-8d0465d6e8cd.png)

### Exemplo de Payload Request:

```
{
  "productId": "Integer",
  "height": "Float",
  "width": "Float",
  "length": "Float",
  "weight: "Float",
}
```

### Exemplo de Response:

```
{
  "productName": "String",
  "height": "Float",
  "width": "Float",
  "length": "Float",
  "weight: "Float",
}
```

### Diagrama de Classe:

![REQ06 DC](https://user-images.githubusercontent.com/101266447/166931555-e7abca06-efeb-47b0-b910-11fc7dc8cc55.png)

### Collection com os End-points no Postman:
Os endpoints do CRUD de dimensão do produto se encontram na pasta "dimensions" no diretório abaixo, dentro do projeto:

```sh
src/main/resources/PostmanCollection.json
```
-----
### Características e Tecnologias:
- Java 11;
- Spring Security e Token JWT;
- Spring Validations;
- Spring Data JPA;
- Banco de Dados relacional Postgres (local).

### Instruções para a instalação:

Para acesso local do banco de dados, é necessário a inserção da variável de ambiente abaixo na IDE:

```sh
HOST=jdbc:postgresql://localhost:5432/PIDB;USERNAME=(seu_nome_de_usuário);PASSWORD=(sua_senha_definida)
```
- Quadro Kanban com as tasks realizadas [Disponível aqui](https://github.com/juliocesargama/ProjetoIntegrador-MeliFrescos/projects/1).

### Diagramas de Classe
![DC](https://user-images.githubusercontent.com/70298438/166481858-c235e35d-9865-4d2c-a556-cd2ab11989a9.jpg)


### Documentação, Referencial utilizados e Cronologia dos requisitos:

[Enunciado Base](https://drive.google.com/file/d/1bBOM49bxqRR7apxP3sgV7_LRiTq9xQD2/view)

[Requisito 1](https://drive.google.com/file/d/1rbT3upYAwN-CrOVtze0M2Fq7Cobuj7FD/view) (Início em: 22/04/22, Término em: 27/04/22)

[Requisito 2](https://drive.google.com/file/d/1M66St3F6TwWJ6WG_s1in75_bMyeKb8PM/view) (Início em: 26/04/22, Término em: 02/05/22)

[Requisito 3](https://drive.google.com/file/d/1GnTl6sHhdvyKjR0oz0nXlyvzH-oW_2Jv/view) (Início em: 28/04/22, Término em: 29/04/22)

[Requisito 4](https://drive.google.com/file/d/1kNZLztafr2tXuDU24W9xwUu09va2kMP0/view) (Início em: 29/04/22, Término em: 02/05/22)

[Requisito 5](https://drive.google.com/file/d/1yiEzdwI87K7AO9bgPffHbb0DPjVKM-oP/view) (Início em: 29/04/22, Término em: 03/05/22)

[Requisito 6](https://drive.google.com/file/d/1zlRtIPjK4r0WdrzFs7LIVA_8Q5HyDgXz/view) (Início em: 03/05/22, Término em: 06/05/22)
