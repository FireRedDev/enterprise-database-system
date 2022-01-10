package at.jku.employeeonboardingsystem.ldap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.List;
import java.util.Optional;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class LDAPRepository {

    @Autowired
    private LdapTemplate ldapTemplate;

    public List<String> getAllPersonNames() {
        return ldapTemplate.search(
            query().where("objectclass").is("person"),
            new AttributesMapper<String>() {
                public String mapFromAttributes(Attributes attrs)
                    throws NamingException {
                    return (String) attrs.get("cn").get();
                }
            });
    }

    public List<Person> getAllPersons() {
        return ldapTemplate.search(query()
            .where("objectclass").is("person"), new PersonAttributesMapper());
    }

    public Person findPerson(String dn) {
        return ldapTemplate.lookup(dn, new PersonAttributesMapper());
    }

    public void updatePerson(Optional<String> id) {
       if(id.isPresent()) {
       }
       else {

       }
    }

    public void deletePerson(Optional<String> id) {

    }

    private class PersonAttributesMapper implements AttributesMapper<Person> {
        public Person mapFromAttributes(Attributes attrs) throws NamingException {
            Person person = new Person();
            person.setFullName((String)attrs.get("cn").get());
            person.setLastName((String)attrs.get("sn").get());
            person.setUid((String)attrs.get("uid").get());
            person.setPassword((String)attrs.get("password").get());
            return person;
        }
    }


/*    public void create(String username, String password) {
        Name dn = LdapNameBuilder
            .newInstance()
            .add("ou", "users")
            .add("cn", username)
            .build();
        DirContextAdapter context = new DirContextAdapter(dn);

        context.setAttributeValues(
            "objectclass",
            new String[]
                { "top",
                    "person",
                    "organizationalPerson",
                    "inetOrgPerson" });
        context.setAttributeValue("cn", username);
        context.setAttributeValue("sn", username);
        context.setAttributeValue
            ("userPassword", org.apache.commons.codec.digest.DigestUtils.sha256Hex(password));

        ldapTemplate.bind(context);
    }
    public void modify(String username, String password) {
        Name dn = LdapNameBuilder.newInstance()
            .add("ou", "users")
            .add("cn", username)
            .build();
        DirContextOperations context
            = ldapTemplate.lookupContext(dn);

        context.setAttributeValues
            ("objectclass",
                new String[]
                    { "top",
                        "person",
                        "organizationalPerson",
                        "inetOrgPerson" });
        context.setAttributeValue("cn", username);
        context.setAttributeValue("sn", username);
        context.setAttributeValue("userPassword",
            org.apache.commons.codec.digest.DigestUtils.sha256Hex(password));

        ldapTemplate.modifyAttributes(context);
    }
    public List<String> search(String username) {
        return ldapTemplate
            .search(
                "ou=users",
                "cn=" + username,
                (AttributesMapper<String>) attrs -> (String) attrs.get("cn").get());
    }*/
}
