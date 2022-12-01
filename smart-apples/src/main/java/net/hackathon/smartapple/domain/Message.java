package net.hackathon.smartapple.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import net.hackathon.smartapple.domain.enumeration.MessageLevel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Message.
 */
@Entity
@Table(name = "message")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private MessageLevel level;

    @Size(max = 256)
    @Column(name = "msg", length = 256)
    private String msg;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Message id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MessageLevel getLevel() {
        return this.level;
    }

    public Message level(MessageLevel level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(MessageLevel level) {
        this.level = level;
    }

    public String getMsg() {
        return this.msg;
    }

    public Message msg(String msg) {
        this.setMsg(msg);
        return this;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Message)) {
            return false;
        }
        return id != null && id.equals(((Message) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Message{" +
            "id=" + getId() +
            ", level='" + getLevel() + "'" +
            ", msg='" + getMsg() + "'" +
            "}";
    }
}
