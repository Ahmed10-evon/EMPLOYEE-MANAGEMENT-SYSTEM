package com.example.employeemanagementsystem.db;

import com.example.employeemanagementsystem.model.Employee;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteDB {

    private static final String URL = buildUrl();

    private static String buildUrl() {
        String dirPath = System.getProperty("user.home") + File.separator + "EmployeeManagementSystem";
        File dir = new File(dirPath);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("Cannot create DB folder: " + dir.getAbsolutePath());
        }

        String dbFile = dirPath + File.separator + "employees.db";
        System.out.println("SQLite DB file = " + dbFile);
        return "jdbc:sqlite:" + dbFile;
    }

    private static void ensureSchema() {
        createTablesIfNeeded();
        seedDefaultAdmin();
    }

    static {
        ensureSchema();
    }


    public static void init() {
        ensureSchema();
        System.out.println("user.home = " + System.getProperty("user.home"));
        System.out.println("db url    = " + URL);

        try (Connection ignored = getConnection()) {
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection c = DriverManager.getConnection(URL);
        try (Statement st = c.createStatement()) {
            st.execute("PRAGMA busy_timeout = 5000");
            st.execute("PRAGMA journal_mode = WAL");
        }
        return c;
    }

    private static void createTablesIfNeeded() {
        String employeesSql = """
                CREATE TABLE IF NOT EXISTS employees (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    email TEXT NOT NULL,
                    department TEXT NOT NULL,
                    salary REAL NOT NULL
                )
                """;

        String adminsSql = """
                CREATE TABLE IF NOT EXISTS admins (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    email TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL
                )
                """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(employeesSql);
            stmt.execute(adminsSql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void seedDefaultAdmin() {
        String sql = "INSERT OR IGNORE INTO admins(email,password) VALUES(?,?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "admin@gmail.com");
            ps.setString(2, "1234");
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------- Admin ----------------

    public static boolean validateAdmin(String email, String password) {
        ensureSchema();
        String sql = "SELECT 1 FROM admins WHERE email = ? AND password = ? LIMIT 1";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean resetAdminPassword(String email, String newPassword) {
        ensureSchema();
        String sql = "UPDATE admins SET password = ? WHERE email = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setString(2, email);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Add admin from UI
    public static boolean addAdmin(String email, String password) {
        ensureSchema();

        String sql = "INSERT INTO admins(email,password) VALUES(?,?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }

    // ---------------- Employees ----------------

    public static void addEmployee(Employee e) {
        ensureSchema();
        String sql = "INSERT INTO employees(name,email,department,salary) VALUES(?,?,?,?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, e.getName());
            ps.setString(2, e.getEmail());
            ps.setString(3, e.getDepartment());
            ps.setDouble(4, e.getSalary());

            int rows = ps.executeUpdate();
            System.out.println("Inserted rows = " + rows);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean updateEmployee(Employee e) {
        ensureSchema();

        String sql = "UPDATE employees SET name=?, email=?, department=?, salary=? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, e.getName());
            ps.setString(2, e.getEmail());
            ps.setString(3, e.getDepartment());
            ps.setDouble(4, e.getSalary());
            ps.setInt(5, e.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static void deleteEmployee(int id) {
        ensureSchema();
        String sql = "DELETE FROM employees WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static List<Employee> getAllEmployees() {
        ensureSchema();
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT id,name,email,department,salary FROM employees ORDER BY id";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Employee(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("department"),
                        rs.getDouble("salary")
                ));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public static Employee getEmployeeById(int id) {
        ensureSchema();
        String sql = "SELECT id,name,email,department,salary FROM employees WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Employee(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("department"),
                            rs.getDouble("salary")
                    );
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static boolean resetEmployees() {
        ensureSchema();

        String deleteAll = "DELETE FROM employees";
        String resetSeq = "DELETE FROM sqlite_sequence WHERE name = 'employees'";

        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {

            conn.setAutoCommit(false);

            st.executeUpdate(deleteAll);
            st.executeUpdate(resetSeq);

            conn.commit();
            conn.setAutoCommit(true);
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
