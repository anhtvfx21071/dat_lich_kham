package com.example.asm3.dao;

import com.example.asm3.dto.AppointmentAdminDto;
import com.example.asm3.dto.DoctorDto;
import com.example.asm3.dto.PatientInformation;
import com.example.asm3.dto.UserDto;
import com.example.asm3.entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class DoctorCareDaoImpl implements DoctorCareDao{
    @Autowired
    private EntityManager entityManager;
    //Lấy khoa nổi bật có nhiều bệnh nhân đăng ký khám
    @Override
    public List<Department> outstandingMedicalDepartment() {
        String queryString = "SELECT d.id , d.name , d.description, d.phone, SUM(count) AS total " +
                "FROM department d " +
                "LEFT JOIN  doctor do ON d.id = do.department_id " +
                "LEFT JOIN ( " +
                "    SELECT doctor_id, COUNT(*) AS count FROM appointment " +
                "    GROUP BY doctor_id) a ON do.id = a.doctor_id " +
                "WHERE d.status=:status GROUP BY d.id, d.name ORDER BY total DESC;";
        Query query = entityManager.createNativeQuery(queryString);
        query.setParameter("status", 1);
        query.setMaxResults(5);
        List<Object[]> resultList = query.getResultList();
        List<Department> departments = new ArrayList<>();
        for (Object[] department: resultList) {
            Department department1 = new Department();
            department1.setId(department[0] != null ? (Integer) department[0] : 0);
            department1.setName((String) department[1]);
            department1.setDescription((String) department[2]);
            department1.setPhone((String) department[3]);
            departments.add(department1);
        }
        return departments;
    }
    //Lấy bệnh viện nổi bật có nhiều khoa
    @Override
    public List<Hospital> outstandingHospital() {
        Query query = entityManager.createQuery("SELECT h.id, h.name,h.address, h.phone, h.description" +
                ",h.status, COUNT(d.id) as count " +
                "FROM Hospital h LEFT JOIN Department d ON h.id = d.hospital.id " +
                "WHERE h.status=:status GROUP BY h.id, h.name order by count desc ");
        query.setParameter("status", 1);
        query.setMaxResults(5);
        List<Object[]> resultList = query.getResultList();
        List<Hospital> hospitals = new ArrayList<>();
        for (Object[] hospital: resultList){
            int id = hospital[0] != null ? (Integer) hospital[0] : 0;
            String name = hospital[1] != null ? String.valueOf(hospital[1]) : "";
            String address = hospital[2] != null ? String.valueOf(hospital[2]) : "";
            String phone = hospital[3] != null ? String.valueOf(hospital[3]) : "";
            String description = hospital[4] != null ? String.valueOf(hospital[4]) : "";
            int status = hospital[5] != null ? (Integer) hospital[5] : 0;
            Hospital hospital1 = new Hospital(id,name,address,phone,description,status);
            hospitals.add(hospital1);
        }
        return hospitals;
    }
    //Tìm kiếm bác sĩ cục bộ
    @Override
    public List<DoctorDto> generalSearch(String key) {
        String query = "SELECT doc.user.name, h.address, d.name, h.name , doc.specialization, doc.user.email, doc.user.phone" +
                " FROM Hospital h RIGHT JOIN Department d ON h.id = d.hospital.id " +
                "RIGHT JOIN Doctor doc ON doc.department.id = d.id " +
                "WHERE CONCAT(COALESCE(h.name, ''), ' ', COALESCE(h.address, ''), ' ', COALESCE(h.description, ''), ' ', COALESCE(h.price, '')) LIKE '%" + key + "%'";
        Query theQuery = entityManager.createQuery(query);
        List<Object[]> resultList = theQuery.getResultList();
        List<DoctorDto> doctorDtos = new ArrayList<>();
        for (Object[] doctor:resultList){
            String doctorName = doctor[0] != null ? String.valueOf(doctor[0]) : "";
            String address = doctor[1] != null ? String.valueOf(doctor[1]) : "";
            String departmentName = doctor[2] != null ? String.valueOf(doctor[2]) : "";
            String hospitalName = doctor[3] != null ? String.valueOf(doctor[3]) : "";
            String specialization = doctor[4] != null ? String.valueOf(doctor[4]) : "";
            String email= doctor[5] != null ? String.valueOf(doctor[5]) : "";
            String phone= doctor[6] != null ? String.valueOf(doctor[6]) : "";
            DoctorDto doctorDto = new DoctorDto(doctorName,address,departmentName,hospitalName,specialization,email,phone);
            doctorDtos.add(doctorDto);
        }
        return doctorDtos;
    }
    //Tìm kiếm bác sỹ theo khoa
    @Override
    public List<DoctorDto> searchByDepartment(String key) {
        String query = "SELECT doc.user.name, d.hospital.address, d.name, d.hospital.name, doc.specialization, doc.user.email, doc.user.phone" +
                " FROM Department d RIGHT JOIN Doctor doc ON d.id = doc.department.id" +
                " WHERE CONCAT(COALESCE(d.name, '')) LIKE '%" + key + "%'";
        Query theQuery = entityManager.createQuery(query);
        List<Object[]> resultList = theQuery.getResultList();
        List<DoctorDto> doctorDtos = new ArrayList<>();
        for (Object[] doctor:resultList){
            String doctorName = doctor[0] != null ? String.valueOf(doctor[0]) : "";
            String address = doctor[1] != null ? String.valueOf(doctor[1]) : "";
            String departmentName = doctor[2] != null ? String.valueOf(doctor[2]) : "";
            String hospitalName = doctor[3] != null ? String.valueOf(doctor[3]) : "";
            String specialization = doctor[4] != null ? String.valueOf(doctor[4]) : "";
            String email= doctor[5] != null ? String.valueOf(doctor[5]) : "";
            String phone= doctor[6] != null ? String.valueOf(doctor[6]) : "";
            DoctorDto doctorDto = new DoctorDto(doctorName,address,departmentName,hospitalName,specialization,email,phone);
            doctorDtos.add(doctorDto);
        }
        return doctorDtos;
    }
    //Đặt lịch khám bệnh
    @Override
    public void bookMedicalExamination(Appointment appointment) {
        entityManager.persist(appointment);
    }
    //Lấy đối tượng bác sĩ bằng id
    @Override
    public Doctor getDoctorByEmail(String email) {
        TypedQuery<Doctor> query = entityManager.createQuery("SELECT d FROM Doctor d JOIN d.user u WHERE u.email = :email", Doctor.class);
        query.setParameter("email", email);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    //Lấy đối tượng user bằng email
    @Override
    public User getUserByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery("FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    //Danh sách bệnh nhân của 1 bác sĩ
    @Override
    public List<PatientInformation> getAppointmentByEmail(String email) {
        Query query = entityManager.createQuery("SELECT a.id, a.user.name, a.user.email, a.user.gender, a.user.address, " +
                "a.title, a.description, a.appointmentDate, a.registration_time, a.status " +
                "FROM Appointment a WHERE a.emailDoctor = :email AND (a.status = 0 OR a.status = 1)");
        query.setParameter("email", email);
        List<Object[]> result = query.getResultList();
        List<PatientInformation> patients = new ArrayList<>();
        for (Object[] patient:result){
            int id = (Integer) patient[0];
            String name = patient[1] != null ? String.valueOf(patient[1]) : "";
            String emailPatient = patient[2] != null ? String.valueOf(patient[2]) : "";
            String gender = patient[3] != null ? String.valueOf(patient[3]) : "";
            String address = patient[4] != null ? String.valueOf(patient[4]) : "";
            String title = patient[5] != null ? String.valueOf(patient[5]) : "";
            String description= patient[6] != null ? String.valueOf(patient[6]) : "";
            LocalDate appointmentDate = patient[7] != null ? (LocalDate) patient[7] : null;
            String registration_time= patient[8] != null ? String.valueOf(patient[8]) : "";
            int status = (Integer) patient[9];
            PatientInformation patient1 = new PatientInformation(id,name, emailPatient,gender,address,title,description,appointmentDate,registration_time,status);
            patients.add(patient1);
        }
        return patients;
    }
    //xác nhận nhận lịch khám bệnh hay hủy
    @Override
    public void changeStatusAppointment(int idAppointment, int status) {
        TypedQuery<Appointment> query = entityManager.createQuery("FROM Appointment u WHERE u.id=:id", Appointment.class);
        query.setParameter("id", idAppointment);
        Appointment appointment = query.getSingleResult();
        appointment.setStatus(status);
        entityManager.merge(appointment);
    }
    //Lưu thông tin bổ sung khi hủy lịch đặt
    @Override
    public void saveExtrainfos(int idAppointment, String description) {
        TypedQuery<Appointment> query = entityManager.createQuery("FROM Appointment u WHERE u.id=:id", Appointment.class);
        query.setParameter("id", idAppointment);
        Appointment appointment = query.getSingleResult();
        Extrainfos extrainfos = new Extrainfos(description,1,appointment.getDoctor(),appointment);
        entityManager.persist(extrainfos);
    }
    //Kiểm tra bác sĩ có thuộc phụ trách lịch hẹn khám bệnh không
    @Override
    public boolean checkAppointmentId(String email, int idAppointment) {
        TypedQuery<Appointment> query = entityManager.createQuery("FROM Appointment a WHERE a.id=:id AND a.emailDoctor=:email", Appointment.class);
        query.setParameter("id", idAppointment);
        query.setParameter("email", email);
        List<Appointment> appointments = query.getResultList();
        return appointments.size() > 0;
    }
    //Lấy danh sách bác sĩ
    @Override
    public List<UserDto> getListDoctor() {
        TypedQuery<Doctor> query = entityManager.createQuery("from Doctor ", Doctor.class);
        List<Doctor> doctors = query.getResultList();
        List<UserDto> userDtos = new ArrayList<>();
        for (Doctor doctor:doctors) {
            UserDto userDto = new UserDto(doctor.getUser().getName(), doctor.getUser().getEmail(), doctor.getUser().getIs_active());
            userDtos.add(userDto);
        }
        return userDtos;
    }
    //Lấy danh sách bệnh nhân
    @Override
    public List<UserDto> getListUser() {
        TypedQuery<User> query = entityManager.createQuery("from User u where u.role = :role", User.class);
        query.setParameter("role", Role.USER);
        List<User> users = query.getResultList();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            UserDto userDto = new UserDto(user.getName(), user.getEmail(), user.getIs_active());
            userDtos.add(userDto);
        }
        return userDtos;
    }
    //Thay đổi trạng thái tài khoản
    @Override
    public boolean changeStatusAccount(String email, int status) {
        try {
            TypedQuery<User> query = entityManager.createQuery("from User u where u.email=:email", User.class);
            query.setParameter("email", email);
            User user = query.getSingleResult();
            user.setIs_active(status);
            if(status == 0){
                user.setDelete_at(LocalDateTime.now());
            }
            entityManager.merge(user);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    //Lấy khoa khám bệnh thông qua id
    @Override
    public Department getDepartmentById(int id) {
        TypedQuery<Department> query = entityManager.createQuery("from Department d where d.id=:id", Department.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }
    //Lưu đối tượng lại
    @Override
    public <T> void save(T t) {
        entityManager.persist(t);
    }

    @Override
    public <T> void update(T t) {
        entityManager.merge(t);
    }

    @Override
    public List<AppointmentAdminDto> getListAppointmentByIdDoctor(int id) {
        TypedQuery<Appointment> query = entityManager.createQuery("from Appointment a where a.doctor.id=:id", Appointment.class);
        query.setParameter("id", id);
        List<Appointment> appointments = query.getResultList();
        List<AppointmentAdminDto> list = new ArrayList<>();
        for (Appointment apointment:appointments) {
            AppointmentAdminDto dto = new AppointmentAdminDto(apointment.getEmailDoctor(), apointment.getTitle(), apointment.getDescription(),
                    apointment.getAppointmentDate(),apointment.getRegistration_time(),apointment.getCreateAt(),apointment.getDeleteAt(),
                    apointment.getStatus(),apointment.getDoctor().getUser().getName(),apointment.getUser().getName());
            list.add(dto);
        }
        return list;
    }

    @Override
    public List<AppointmentAdminDto> getListAppointmentByIdUser(int id) {
        TypedQuery<User> query = entityManager.createQuery("from User u where u.id=:id", User.class);
        query.setParameter("id", id);
        User user = query.getSingleResult();
        List<Appointment> appointments = user.getAppointments();
        List<AppointmentAdminDto> list = new ArrayList<>();
        for (Appointment apointment:appointments) {
            AppointmentAdminDto dto = new AppointmentAdminDto(apointment.getEmailDoctor(), apointment.getTitle(), apointment.getDescription(),
                    apointment.getAppointmentDate(),apointment.getRegistration_time(),apointment.getCreateAt(),apointment.getDeleteAt(),
                    apointment.getStatus(),apointment.getDoctor().getUser().getName(),apointment.getUser().getName());
            list.add(dto);
        }
        return list;
    }

}
