package sg.edu.np.mad.evap2;

public class forumclass {
    private String category, title, content, answer,status;

    public forumclass(){}

    public forumclass(String category, String title, String content, String answer, String status){
        this.category = category;
        this.title = title;
        this.content = content;
        this.answer = answer;
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
