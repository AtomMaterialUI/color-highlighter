<script>
  import { writable } from 'svelte/store';

  const isValid = writable(true); // you can customize the initial value.
</script>

<style>
  label[aria-invalid="true"] {
    color: red;
  }

  label[aria-invalid="false"] {
    color: black;
  }
</style>


<div aria-invalid={$isValid} style="color: red"/>
