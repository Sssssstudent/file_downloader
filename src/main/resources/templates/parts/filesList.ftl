<table>
    <thead>
    <tr>
        <th>id</th>
        <th>File Name</th>
    </tr>
    </thead>
    <tbody>
    <#list files as file>
        <tr>
            <td>
                <a href="/files/${file.id}"<b>${file.originalName}</b></a>
            </td>
            <td>
                <button onclick="deleteData(${file.id})">Delete</button>
            </td>
        </tr>
    </#list>
    </tbody>
</table>

<script type="text/javascript">
    function deleteData(id) {
        fetch('/files/' + id, {
            method: 'DELETE'
        });
    }
</script>
