package at.jku.employeeonboardingsystem.ldap;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.List;
import java.util.Optional;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.ldap.LdapName;
import liquibase.pro.packaged.id;
import liquibase.pro.packaged.p;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.core.support.BaseLdapNameAware;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

@Service
public class LDAPRepository implements BaseLdapNameAware {

    public void setBaseLdapPath(LdapName baseLdapPath) {
        this.baseLdapPath = baseLdapPath;
    }

    private final Logger log = LoggerFactory.getLogger(LDAPRepository.class);

    @Autowired
    private LdapTemplate ldapTemplate;

    private LdapName baseLdapPath;
    String ldapName = "dc=memorynotfound,dc=com";

    private Name buildDn(Person p) {
        return LdapNameBuilder.newInstance(ldapName).add("ou", "people").add("uid", p.getUid()).build();
    }

    public void create(Person p) {
        Name dn = buildDn(p);
        ldapTemplate.bind(dn, null, buildAttributes(p));
    }

    public List<String> getAllPersonNames() {
        return ldapTemplate.search(
            query().where("objectclass").is("person"),
            new AttributesMapper<String>() {
                public String mapFromAttributes(Attributes attrs) throws NamingException {
                    return (String) attrs.get("cn").get();
                }
            }
        );
    }

    public List<Person> getAllPersons() {
        return ldapTemplate.search(query().where("objectclass").is("person"), new PersonAttributesMapper());
    }

    public Person findPerson(String uid) {
        Name dn = LdapNameBuilder.newInstance(ldapName).add("ou", "people").add("uid", uid).build();
        return ldapTemplate.lookup(dn, new PersonContextMapper());
    }

    public void updatePerson(Optional<Person> p) {
        if (p.isPresent()) ldapTemplate.rebind(buildDn(p.get()), null, buildAttributes(p.get()));
    }

    public void deletePerson(Optional<String> id) {
        if (id.isPresent()) {
            Person p = findPerson(id.get());
            Name dn = buildDn(p);
            ldapTemplate.unbind(dn);
        }
    }

    private class PersonAttributesMapper implements AttributesMapper<Person> {

        public Person mapFromAttributes(Attributes attrs) throws NamingException {
            Person person = new Person();
            person.setFullName((String) attrs.get("cn").get());
            person.setLastName((String) attrs.get("sn").get());
            person.setUid((String) attrs.get("uid").get());
            person.setPassword((String) attrs.get("password").get());
            return person;
        }
    }

    private static class PersonContextMapper extends AbstractContextMapper<Person> {

        public Person doMapFromContext(DirContextOperations context) {
            Person person = new Person();
            person.setFullName(context.getStringAttribute("cn"));
            person.setLastName(context.getStringAttribute("sn"));
            person.setUid(context.getStringAttribute("uid"));
            return person;
        }
    }

    private Attributes buildAttributes(Person p) {
        Attributes attrs = new BasicAttributes();
        BasicAttribute ocAttr = new BasicAttribute("objectclass");
        ocAttr.add("top");
        ocAttr.add("person");
        attrs.put(ocAttr);
        attrs.put("ou", "people");
        attrs.put("uid", p.getUid());
        attrs.put("cn", p.getFullName());
        attrs.put("sn", p.getLastName());
        return attrs;
    }
    /*
     * public void create(String username, String password) {
     * Name dn = LdapNameBuilder
     * .newInstance()
     * .add("ou", "users")
     * .add("cn", username)
     * .build();
     * DirContextAdapter context = new DirContextAdapter(dn);
     *
     * context.setAttributeValues(
     * "objectclass",
     * new String[]
     * { "top",
     * "person",
     * "organizationalPerson",
     * "inetOrgPerson" });
     * context.setAttributeValue("cn", username);
     * context.setAttributeValue("sn", username);
     * context.setAttributeValue
     * ("userPassword",
     * org.apache.commons.codec.digest.DigestUtils.sha256Hex(password));
     *
     * ldapTemplate.bind(context);
     * }
     * public void modify(String username, String password) {
     * Name dn = LdapNameBuilder.newInstance()
     * .add("ou", "users")
     * .add("cn", username)
     * .build();
     * DirContextOperations context
     * = ldapTemplate.lookupContext(dn);
     *
     * context.setAttributeValues
     * ("objectclass",
     * new String[]
     * { "top",
     * "person",
     * "organizationalPerson",
     * "inetOrgPerson" });
     * context.setAttributeValue("cn", username);
     * context.setAttributeValue("sn", username);
     * context.setAttributeValue("userPassword",
     * org.apache.commons.codec.digest.DigestUtils.sha256Hex(password));
     *
     * ldapTemplate.modifyAttributes(context);
     * }
     * public List<String> search(String username) {
     * return ldapTemplate
     * .search(
     * "ou=users",
     * "cn=" + username,
     * (AttributesMapper<String>) attrs -> (String) attrs.get("cn").get());
     * }
     */
}
