Specification resolver spring boot starter
---

The library allows you to use [Specification](https://docs.spring.io/spring-data/jpa/reference/jpa/specifications.html)
from Spring Data JPA as an argument in the controller to filter JPA entity.

The library heavily inspired by [specification-arg-resolver](https://github.com/tkaczmarzyk/specification-arg-resolver),
but unlike it, filters are defined inside entities and all filters are connected using the AND operation. This approach
is more suitable if you are creating separate read-only entities to output a filtered list to the UI.

## Basic usage

Let's imagine that we want to display a table of users in the UI from a database view, and we want to filter the table
by any field or combination of fields. In this case HTTP request can look like this:

```
GET /search?nameLike=Jo&ageFrom=25&ageTo=40
```

To specify a match between the parameters from the HTTP request and the fields of our JPA entity we use the `@Filter`
annotation:

```java

@Entity
public static class User {
    @Id
    @Filter(filter = Equal.class)
    private Long id;
    @Filter(name = "nameLike", filter = LikeIgnoreCase.class)
    private String name;
    @Filter(name = "ageFrom", filter = GreaterThan.class)
    @Filter(name = "ageTo", filter = LessThan.class)
    private int age;
}
```

The `name` field in the annotation must match the name of the HTTP request parameter. If `name` field is
not specified, the entity field name is used instead.

To handel HTTP request, let's add a controller that takes Specification<User> as an argument:

```java

@RestController
public static class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/search")
    public List<User> search(Specification<User> spec) {
        return userRepository.findAll(spec);
    }
}
```

And the standard Spring Data Jpa repository interface for User entity:

```java
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
}
```

As a result of calling the HTTP method, the following JPA request will be generated:

```sql
select u from User u where u.name like '%Jo%' and u.age > 25 and u.age < 40 and u.role in ('QA', 'MANAGER');
```

Only fields specified as parameters in the request will participate in the SQL query.

## Create custom filter

To create your own filter, you must implement
the [FieldFilter](src/main/java/io/github/neherim/spec/resolver/filter/FieldFilter.java) interface.
Or you can extend one of the abstract classes from [common](src/main/java/io/github/neherim/spec/resolver/filter/common)
package.