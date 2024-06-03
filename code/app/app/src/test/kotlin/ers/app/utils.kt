package ers.app

import ers.app.repo.mappers.*
import org.jdbi.v3.core.Jdbi
import org.postgresql.ds.PGSimpleDataSource

fun jdbiSetup(): Jdbi {
    val jdbcDatabaseUrl = System.getenv("ERS_TEST_DATABASE_URL")
    val dataSource = PGSimpleDataSource()
    dataSource.setURL(jdbcDatabaseUrl)

    val jdbi = Jdbi.create(dataSource)
    jdbi.registerRowMapper(UserDtoMapper())
    jdbi.registerRowMapper(RobotDtoMapper())
    jdbi.registerRowMapper(TaskDtoMapper())
    jdbi.registerRowMapper(TimeDtoMapper())
    jdbi.registerRowMapper(AreaDtoMapper())

    return jdbi
}

fun insert(jdbi: Jdbi) {
    val pass1 = "123456".hashCode()
    val pass2 = "pass".hashCode()
    jdbi.useHandle<Exception> { handle ->
        //users
        handle.execute("insert into USERS (name, hashPass, email, token) values ('Maria', $pass1 ,'maria@gmail.com', '1')")
        handle.execute("insert into USERS (name, hashPass, email, token) values ('João', $pass2 ,'joao@gmail.com', '12')")

        //robots
        handle.execute("insert into ROBOT (name, status, characteristics) values ('Robot1', 'available', 'plastic')")
        handle.execute("insert into ROBOT (name, status, characteristics) values ('Robot2', 'available', 'glass')")
        handle.execute("insert into ROBOT (name, status, characteristics) values ('Robot3', 'available', 'paper')")

        //tasks
        handle.execute("insert into TASK (userid, robotid, name, status) values (1, 1,'Task1', 'pending')")
        handle.execute("insert into TASK (userid, robotid, name, status) values (1, 2,'Task2', 'pending')")
        handle.execute("insert into TASK (userid, robotid, name, status) values (2, 3,'Task3', 'pending')")
        handle.execute("insert into TASK (userid, robotid, name, status) values (1, 1,'Task4', 'pending')")

        //time
        handle.execute("insert into TIME (taskid, weekday, description, start_time, end_time) values (1, 'Friday','time1','10:00:00', '12:00:00')")
        handle.execute("insert into TIME (taskid, weekday, description, start_time, end_time) values (2, 'Friday','time2','10:00:00', '12:00:00')")
        handle.execute("insert into TIME (taskid, weekday, description, start_time, end_time) values (3, 'Friday','time3','10:00:00', '12:00:00')")
        handle.execute("insert into TIME (taskid, weekday, description, start_time, end_time) values (3, 'Monday','time4','10:00:00', '12:00:00')")

        //areas
        handle.execute("insert into AREA (taskid,  name, description, height, width) values (1, 'Area1', 'description1', 10, 10)")
        handle.execute("insert into AREA (taskid,  name, description, height, width) values (2, 'Area2', 'description2', 10, 10)")
        handle.execute("insert into AREA (taskid,  name, description, height, width) values (3, 'Area3', 'description3', 10, 10)")
        handle.execute("insert into AREA (taskid,  name, description, height, width) values (3, 'Area4', 'description4', 10, 10)")
    }
}


fun delete(jdbi: Jdbi) {
    jdbi.useHandle<Exception> { handle ->

        handle.execute("delete from AREA cascade")
        handle.execute("delete from TIME cascade")
        handle.execute("delete from TASK cascade")
        handle.execute("delete from ROBOT cascade")
        handle.execute("delete from USERS cascade")

        handle.execute("SELECT setval('users_id_seq', 1, false)")
        handle.execute("SELECT setval('robot_id_seq', 1, false)")
        handle.execute("SELECT setval('task_id_seq', 1, false)")
        handle.execute("SELECT setval('time_id_seq', 1, false)")
        handle.execute("SELECT setval('area_id_seq', 1, false)")
    }
}
