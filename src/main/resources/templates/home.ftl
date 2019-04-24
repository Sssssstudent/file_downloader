<#import "parts/common.ftl" as c>
<@c.page>
    <div>
        <form method="post" enctype="multipart/form-data">
            <input type="file" name="file" placeholder="Select a file">
            <button type="submit">Add</button>
        </form>
    </div>
    <div>List of uploaded files</div>
    <#list files as file>
        <div>
            <b>${file.id}</b>
            <span>${file.fileName}</span>
        </div>
    </#list>
</@c.page>
