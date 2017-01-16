package delta_service.query;

import org.openrdf.model.Value;

/**
 * Created by langens-jonathan on 31.05.16.
 *
 * This is a datastructure representing a triple. It consists of 5 private string
 * objects. All other code is autogenerated with exception of the setObject function
 * that also sets the type and value.
 *
 * TODO make this more representative of what a triple might be and support the different forms it may have
 *      in a more logical fashion (for instance the difference between a literal and a uri for the object
 *      support for types and languages...
 *
 *  TODO maybe I can introduce a new class called a term that consists of a String and a Type
 *  TODO and then the subject and the predicate would be URI's by default
 */
public class Triple
{
    // the subject
    private String subject;

    // the predicate
    private String predicate;

    // the object (it is set as a value rather than a string
    // as it has a value and a type
    private Value object = null;

    // the string representation for the object
    private String objectString = "";

    // the string representation of the type of the object
    private String objectType = "";

    /**
     * @return this.subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * removes the ':' from the subject if it has it and assigns the value to this.subject
     * @param subject the new subject value for this triple
     */
    public void setSubject(String subject) {
        if(subject.startsWith(":"))
        {
            subject = subject.substring(1, subject.length());
        }
        this.subject = subject;
    }

    /**
     * @return this.object
     */
    public Value getObject() {
        return object;
    }

    /**
     * Sets this.object, but also assigns the string representation to this.objectString and the
     * type of the passed object to this.objectType
     * @param object a Value representation for the object
     */
    public void setObject(Value object) {
        this.object = object;
        this.objectString = object.stringValue();
        if(this.objectString.startsWith(":"))
        {
            this.objectString = this.objectString.substring(1, this.objectString.length());
        }
        this.objectType = object.toString().substring(0, this.objectString.length());
    }

    /**
     * Sets the object as a string, if a type is passed using the SPARQL ^^ operator than this
     * is also correctly parsed and assigned to objectType
     * @param fullObject the String representation for the object
     */
    public void setObject(String fullObject)
    {
        if(fullObject.indexOf("^^") > -1)
        {
            int index = fullObject.indexOf("^^");
            this.setObjectString(fullObject.substring(0, index));
            this.setObjectType(fullObject.substring(index + 2, fullObject.length()));
        }
        else if(fullObject.indexOf("@") > -1)
        {
            this.setObjectString(fullObject.substring(0, fullObject.length()));
            this.setObjectType("literal");
        }
        else
        {
            this.setObjectString(fullObject);
        }
    }

    /**
     * @return this.predicate
     */
    public String getPredicate() {
        return predicate;
    }

    /**
     * sets the predicate string and removes the first character if it is a ':'
     * @param predicate the predicate string
     */
    public void setPredicate(String predicate) {
        if(predicate.startsWith(":"))
        {
            predicate = predicate.substring(1, predicate.length());
        }
        this.predicate = predicate;
    }

    /**
     * directly sets the object string
     * note however that it is prefered to use the setObject method as this does not parse
     * typing
     *
     * @param objectString an string representation of the BARE object (so no ^^type...)
     */
    public void setObjectString(String objectString){
        if(objectString.startsWith(":"))
        {
            objectString = objectString.substring(1, objectString.length());
        }
        this.objectString = objectString;
    }

    /**
     * There are 3 possibilites when we want to convert the object to a string
     *  - it is a literal string, in that case it should start and end with a quote
     *  - it is a uri in that case it should start and and with a < or >
     *  - it has a body and a suffix separated by a @
     *
     * @return a correct string representation of the object
     */
    public String getObjectAsString()
    {
        String toreturn = this.objectString;
        if(toreturn.contains("@"))
        {
            String body = this.objectString.substring(0, this.objectString.indexOf("@"));
            String suffix = this.objectString.substring(this.objectString.indexOf("@"), this.objectString.length());
            if(!body.startsWith("\""))
                body = "\"" + body;
            if(!body.substring(1, body.length()).endsWith("\""))
                body = body + "\"";
            return body + suffix;
        }
        if(toreturn.endsWith("\"") || toreturn.startsWith("\""))
        {
            if(!toreturn.endsWith("\""))
                toreturn += "\"";
            if(!toreturn.startsWith("\""))
                toreturn = "\"" + toreturn;
            return toreturn;
        }
        if(objectType.equalsIgnoreCase("literal"))
        {
            return "\"" + objectString + "\"";
        }
        if(!toreturn.startsWith("<"))
            toreturn = "<" + toreturn;
        if(!toreturn.endsWith(">"))
            toreturn += ">";
        return toreturn;
    }

    /**
     * not the prefered method for obtaining the object string, the default method is getObjectAsString
     * @return this.objectString
     */
    public String getObjectString() {return this.objectString;}

    /**
     * sets the objectType
     * @param objectType a SPARQL compliant representation for the object type
     */
    public void setObjectType(String objectType) { this.objectType = objectType;}

    /**
     * @return this.objectType
     */
    public String getObjectType() {
        return this.objectType;}
}
