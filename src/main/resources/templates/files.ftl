<#import "parts/common.ftl" as c>
<@c.page>
    <div>Add file</div>
    <form method="post" enctype="multipart/form-data">
        <input type="file" name="file" placeholder="Выберете файл">
        <input type="hidden" name="id" value="<#if file??>${file.id}</#if>" />
        <button type="submit">Add</button>
    </form>


    <#include "parts/filesList.ftl">
</@c.page>
