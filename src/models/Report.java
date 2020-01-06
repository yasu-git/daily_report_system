package models;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "reports")

@NamedQueries({
        @NamedQuery(
                name = "getAllReports",
                query = "SELECT r FROM Report AS r ORDER BY r.id DESC"
                ),
        @NamedQuery(
                name = "getReportsCount",
                query = "SELECT COUNT(r) FROM Report AS r"
                ),
        //ログインしている人のレポートを取得
        @NamedQuery(
                name = "getMyAllReports",
                query = "SELECT r FROM Report AS r WHERE r.employee = :employee ORDER BY r.id DESC"
                ),
        //ログインしている人のレポートの数を取得
        @NamedQuery(
                name = "getMyReportsCount",
                query = "SELECT COUNT(r) FROM Report AS r WHERE r.employee = :employee"
                )

})

@Entity

public class Report {
    /*
     * @GeneratedValue
     * 値を自動で生成し、@Idに適用
     * GenerationType.IDENTITY
     * primary key 設定 と not null指定
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /*
     * 日報の作成者情報
     * employeeプロパティが作成者情報
     * ログインしている従業員情報をオブジェクトのまま格納する
     * @ManyToOneは「多対１」であることを表す
     * employeeが「多」なので先にMany
     * @JoinColumnは
     */
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    //年月日のみを管理(Date)
    @Column(name = "report_date", nullable = false)
    private Date report_date;

    //length = 255文字設定
    @Column(name = "title", length = 255, nullable = false)
    private String title;

    /*
     * @Lobを指定することで
     * テキストエリア指定を行う
     * この指定をすることで改行もデータベースに保存される
     */
    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    //年月日の他に時間分秒(Timestamp)
    @Column(name = "created_at", nullable = false)
    private Timestamp created_at;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updated_at;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Date getReport_date() {
        return report_date;
    }

    public void setReport_date(Date report_date) {
        this.report_date = report_date;
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

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

}
