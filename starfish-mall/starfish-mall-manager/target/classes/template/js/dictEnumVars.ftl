var Enums = {};
<#list enumVarNames as enumVarName >
Enums.${enumVarName} = ${dictEnumVarJsons["${enumVarName}"]};
</#list>