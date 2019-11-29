<#compress>
[
    <#if dataModel??>
        <#list dataModel as teacher>
        {
            <#if teacher.name??>
                "name": "${teacher.name?j_string}",
            </#if>
            <#if teacher.age??>
                "age": ${teacher.age},
            </#if>
            <#if teacher.gender??>
                "sex": "${teacher.gender?j_string}",
            <#else>
                "sex": "male",
            </#if>
            <#if teacher.course??>
                "course": "${teacher.course?j_string}"
            </#if>
        }<#if teacher?has_next>,</#if>
        </#list>
    </#if>
]
</#compress>