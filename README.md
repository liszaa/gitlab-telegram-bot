# TelegramBot для gitlab

Находит все ветки, подходящие по паттерну, в группе проектов в гитлабе и отправляет результат в телеграм

### Один из usercase'ов 

##### Пролог  
Ваш проект состоит из 15 репозиториев в гитлабе и все они лежат в одной группе `projectName`  
Вы сделали задачу, которая затронула 5 репозиториев. Bетка называется `666-is-my-favourite-number`
##### Проблема  
Вам нужно влить таск в тестовые ветки в каждом проекте, но вот незадача, вы совершенно не помните, какие проекты затронули
##### Решение
Отправляете боту сообщение `/branches 666` и получаете список репозиториев с веткой, в названии которой есть `666`



## Getting Started

#### 1.  Добавляем в проект mvn dependency  
#### 2.  Создаем бота и токен в гитлабе
Бот создается через @BotFather. После создания понадобится токен, имя бота и создать команду `/branches`  
Токен для группы в гитлабе можно создать [так](https://docs.gitlab.com/ee/user/group/settings/group_access_tokens.html#create-a-group-access-token-using-ui)
#### 2.  Определяем необходимые environment variables

| Name                       | Default value | Description                                                                     |
|----------------------------|---------------|---------------------------------------------------------------------------------|
| gitlab.group.name          | empty         | Название группы в гитлабе (нужно для запроса в гитлаб)                          |
| gitlab.group.token         | empty         | Токен для получения инфы от гитлаба                                             |
| gitlab.group.graphql_url   | empty         | URL для графкл запросов к гитлабу: `https://<your-gitlab-site.com>/api/graphql` |
| telegram.bot.name          | empty         | Имя бота после `@`                                                              |
| telegram.bot.token         | empty         | Токен от созданного бота                                                        |
| telegram.bot.allowed_chats | empty         | массив id чатов, в которых разрешено использовать бота                          |


## Как работает

Бот реагирует на команды, которые заведены через @BotFather и обработка реализована в коде  
За каждую команду отвечает свой handler, который должен наследоваться от `CommandHandler`  
По дефолту реализована только команда `/branches`, которая обрабатывается `BranchesHandler`, но ничего не мешает вам написать свой хэндлер и реализовать новую команду  

P.S. Лучше сделать так, чтобы у бота не было доступа к сообщениям в чате и он реагировал только на команды.
Настраивается это через BotFather – privacy mode  
P.P.S. Бот не работает в лс 