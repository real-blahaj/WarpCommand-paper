# pxewarps

a slightly customizable warp plugin

## features

### for players

- warp to positions set by admins using `/warp <warp>`
- list all warps (you have permission to teleport to) with `/warps`

### for admins

- set warps using `/setwarp <name>`
- use minimessage to style the name (`/setwarp <warp> displayname <name>`) and set the description (`/setwarp <warp> description <description>`) of warps.
- require that players have permission to visit/see a warp with `/setwarp <warp> permission <node>`
- delete a warp with `/delwarp <warp>`

<details>
  <summary>click to view commands/permissions</summary>
  <h3><code>/warp &lt;warp&gt;</code></h3>
  teleport to a warp
  <ul>
      <li><strong>permission:</strong> <code>warps.warp</code></li>
  </ul>

<h3><code>/warps</code></h3>
view all available warps
  <ul>
      <li><strong>permission:</strong> <code>warps.warp</code></li>
      <li><strong>aliases:</strong> <code>/warp</code></li>
  </ul>

<h3><code>/warps reload</code></h3>
reloads the <code>warps.yml</code> file
  <ul>
      <li><strong>permission:</strong> <code>warps.reload</code></li>
  </ul>

<h3><code>/setwarp &lt;name&gt;</code></h3>
creates a new warp
<ul>
<li><strong>permission:</strong> <code>warps.create</code></li>
</ul>
<h3><code>/setwarp &lt;warp&gt; ...</code></h3>
modifies an existing warp
  <ul>
      <li><h4><code>location [&lt;location&gt;]</code></h4>
      change the location of a warp. if no location is given, the executor's location will be used instead
      <ul>
          <li><strong>permission:</strong> <code>warps.set.location</code></li>
          <li><strong>aliases:</strong> <code>/setwarp &lt;warp&gt;</code></li>
      </ul></li>
      <li><h4><code>displayname [&lt;displayname&gt;]</code></h4>
      change the display name of a warp. minimessage can be used in the display name to style it. if no display name is given, the display name will be reset.
      <ul>
          <li><strong>permission:</strong> <code>warps.set.displayname</code></li>
      </ul></li>
      <li><h4><code>description [&lt;description&gt;]</code></h4>
      change the description of a warp as displayed when its name is hovered over. minimessage can be used in the description to style it. if no description is given, the description will be reset.
      <ul>
          <li><strong>permission:</strong> <code>warps.set.description</code></li>
      </ul></li>
      <li><h4><code>permission [&lt;permission&gt;]</code></h4>
      change the permission required to visit a warp. if no permission is given, the permission will be reset.
      <ul>
          <li><strong>permission:</strong> <code>warps.set.permission</code></li>
      </ul></li>
  </ul>

<h3><code>/delwarp &lt;warp&gt;</code></h3>
deletes a warp
  <ul>
      <li><strong>permission:</strong> <code>warps.delete</code></li>
  </ul>
</details>