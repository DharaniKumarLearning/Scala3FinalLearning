package com.DatabaseOperations

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import org.flywaydb.core.Flyway
import java.sql.{Connection, PreparedStatement, ResultSet}

object DatabaseApp {
  def main(args: Array[String]): Unit = {

    val dbUrl = "jdbc:postgresql://localhost:5432/mydatabase"
    val dbUser = "myuser"
    val dbPass = "mypassword"

    // Step 1: Run Flyway Migration
    def runMigrations(): Unit = {
      println("Running database migrations...")
      val flyway = Flyway.configure()
        .dataSource(dbUrl, dbUser, dbPass)
        .locations("classpath:db/migration")
        .load()

      val migrationsApplied = flyway.migrate().migrationsExecuted
      println(s"Migrations completed. Applied $migrationsApplied scripts.")
    }

    // Step 2: Initialize HikariCP Connection Pool
    def createDataSource(): HikariDataSource = {
      val config = new HikariConfig()
      config.setJdbcUrl(dbUrl)
      config.setUsername(dbUser)
      config.setPassword(dbPass)
      config.setDriverClassName("org.postgresql.Driver")

      // HikariCP tuning defaults
      config.setMaximumPoolSize(10)
      config.setMinimumIdle(2)
      config.setIdleTimeout(300000)
      config.setConnectionTimeout(20000)

      new HikariDataSource(config)
    }

    // Run migrations first
    runMigrations()
    val dataSource = createDataSource()

    def insertDataToUsersTable() : Unit = {
      try {
        val connection: Connection = dataSource.getConnection()
        try {
          val insertSql = "INSERT INTO users (name, email) VALUES (?, ?)"
          val prepStmt: PreparedStatement = connection.prepareStatement(insertSql)

          val usersToInsert = Seq(
            ("David Miller", "david@example.com"),
            ("Eva Green", "eva@example.com")
          )

          for ((name, email) <- usersToInsert) {
            prepStmt.setString(1, name)
            prepStmt.setString(2, email)
            prepStmt.addBatch()
          }

          val rowsInserted = prepStmt.executeBatch().sum
          println(s"Successfully inserted $rowsInserted new users from Scala code!")
          prepStmt.close()

          // 5. Query and Display All Records
          val selectSql = "SELECT id, name, email, created_at FROM users ORDER BY id ASC"
          val queryStmt = connection.createStatement()
          val rs: ResultSet = queryStmt.executeQuery(selectSql)

          println("\n--- Current Users in Database ---")
          while (rs.next()) {
            val id = rs.getInt("id")
            val name = rs.getString("name")
            val email = rs.getString("email")
            val createdAt = rs.getTimestamp("created_at")
            println(s"ID: $id | Name: $name | Email: $email | Created: $createdAt")
          }
          queryStmt.close()

        } finally {
          connection.close()
        }

      } finally {
        dataSource.close()
      }
    }

    insertDataToUsersTable()
  }
}
