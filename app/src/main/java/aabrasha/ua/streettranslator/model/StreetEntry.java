package aabrasha.ua.streettranslator.model;

/**
 * Created by Andrii Abramov on 8/27/16.
*/
public class StreetEntry {

    private Integer id;
    private String oldName;
    private String newName;
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StreetEntry that = (StreetEntry) o;

        if (oldName != null ? !oldName.equals(that.oldName) : that.oldName != null) return false;
        if (newName != null ? !newName.equals(that.newName) : that.newName != null) return false;
        return description != null ? description.equals(that.description) : that.description == null;

    }

    @Override
    public int hashCode() {
        int result = oldName != null ? oldName.hashCode() : 0;
        result = 31 * result + (newName != null ? newName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StreetEntry{" +
                "id=" + id +
                ", oldName='" + oldName + '\'' +
                ", newName='" + newName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
