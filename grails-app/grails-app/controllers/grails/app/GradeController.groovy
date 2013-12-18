package grails.app



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class GradeController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Grade.list(params), model: [gradeInstanceCount: Grade.count()]
    }

    def show(Grade gradeInstance) {
        respond gradeInstance
    }

    def create() {
        respond new Grade(params)
    }

    @Transactional
    def save(Grade gradeInstance) {
        if (gradeInstance == null) {
            notFound()
            return
        }

        if (gradeInstance.hasErrors()) {
            respond gradeInstance.errors, view: 'create'
            return
        }

        gradeInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'gradeInstance.label', default: 'Grade'), gradeInstance.id])
                redirect gradeInstance
            }
            '*' { respond gradeInstance, [status: CREATED] }
        }
    }

    def edit(Grade gradeInstance) {
        respond gradeInstance
    }

    @Transactional
    def update(Grade gradeInstance) {
        if (gradeInstance == null) {
            notFound()
            return
        }

        if (gradeInstance.hasErrors()) {
            respond gradeInstance.errors, view: 'edit'
            return
        }

        gradeInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Grade.label', default: 'Grade'), gradeInstance.id])
                redirect gradeInstance
            }
            '*' { respond gradeInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Grade gradeInstance) {

        if (gradeInstance == null) {
            notFound()
            return
        }

        gradeInstance.delete flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Grade.label', default: 'Grade'), gradeInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'gradeInstance.label', default: 'Grade'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
