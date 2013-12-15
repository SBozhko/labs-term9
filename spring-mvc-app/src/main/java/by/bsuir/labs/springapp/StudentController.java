package by.bsuir.labs.springapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StudentController {
    @Autowired
    private StudentDao studentDao;

    @RequestMapping(value = "/students", method = RequestMethod.GET)
    public String listStudents(ModelMap model) {
        model.addAttribute("student", new Student());
        model.addAttribute("students", studentDao.findAll());
        return "students";
    }

    @RequestMapping(value = "/api/students", method = RequestMethod.GET)
    @ResponseBody
    public String listStudentsJson(ModelMap model) throws JSONException {
        JSONArray studentArray = new JSONArray();
        for (Student student : studentDao.findAll()) {
            JSONObject studentJson = new JSONObject();
            studentJson.put("id", student.getId());
            studentJson.put("lastName", student.getLastName());
            studentJson.put("firstName", student.getFirstName());
            studentJson.put("middleName", student.getMiddleName());
            studentJson.put("birthday", student.getBirthday());
            studentJson.put("gpa", student.getGpa());
            studentArray.put(studentJson);
        }
        return studentArray.toString();
    }

    @RequestMapping(value = "/students/add", method = RequestMethod.POST)
    public String addStudent(@ModelAttribute("student") Student student, BindingResult result) {
        studentDao.save(student);
        return "redirect:/students";
    }

    @RequestMapping("/students/delete/{studentId}")
    public String deleteStudent(@PathVariable("studentId") Long studentId) {
        studentDao.delete(studentId);
        return "redirect:/students";
    }
}
