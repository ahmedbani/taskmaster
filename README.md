# Task-Master

## Description 

Task-Master is an application where you can organize tasks and also add tasks to the app 
<div style="width: 40%; margin-right: 3%;">

![home screen](screenshots/homeScreen.png)  
</div>

## version 1.0.0

- the add task when you click it opens a form to add a task 

<div style="width: 40%; margin-right: 3%;"> 

![add task](screenshots/addTask.png)  
</div>
- the all tasks where you can see all the tasks  

<div style="width: 40%; margin-right: 3%;">

![all tasks](screenshots/allTasks.png)  
</div>

## version 1.0.1

- added a new feature which is the settings where you can write your name and save then it will show the user name in the home screen  

<div style="width: 40%; margin-right: 3%;">

![settings](screenshots/settings.png)  
</div>
- added 3 new tasks when you press on them they will show you the details of that task  
<div style="width: 40%; margin-right: 3%;">

![task](screenshots/task.png)  
</div>

## version 1.0.2  

The home screen have been modified instead of having buttons for tasks all over the home screen there will be a scroll down box that contains the tasks, for the tasks it will show the title of the task, the body body of the task and its state, when you click on one of the tasks it will show you its details. 

## version 1.0.3

+ **Homepage**
The main page should be modified to contain a rendered list of tasks (title, body and state) inside a recycler view which are retrieved from the Room database. When a user taps one of the titles, it should go to the Task Detail page, which desplays the task details.

+ **AddTask**
In this activity you can add a new task by adding it's title, body and state in the input fields, and click submit. The data will be stored inside the Room database. 

<div style="width: 40%; margin-right: 3%;">

![addTaskPage](screenShots/addTaskPage.png)
</div>

+ **Task Detail Page**
Create a Task Detail page. It should have a title at the top of the page, with the state beside it, and the body underneath.  

<div style="width: 40%; margin-right: 3%;">

![detail](screenShots/taskDetail.png)
</div>

## version 1.0.4

+ Modified the Add Task form to save the data entered in as a Task to DynamoDB.

+ Refactor the homepage’s RecyclerView to display all Task entities in DynamoDB.

## version 1.0.5

+ Modified the Add Task form to have radio buttons to select the team the task is for and save the data entered in as a Task to DynamoDB with the task having a relation with the team.

<div style="width: 40%; margin-right: 3%;">

![detail](screenShots/addTeam.png)

</div>

+ modified the settings page now it shows you the teams you can choose one of them.

<div style="width: 40%; margin-right: 3%;">

![detail](screenShots/settingTeam.png)
</div>

+ Refactor the homepage’s RecyclerView to display all Task entities in DynamoDB for the team you choose in the settings.

## version 1.0.5

+ added authentication service, now the user can signup and login to his account 

## Devs 

- **Ahmed Bani-Salameh**