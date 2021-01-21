Welcome to the button to action app.
This app utilizes the new DI "Hilt" by Google,
MVVM architecture,
Room 2.3,
retrofit 2,
Junit and Mocito.

At the first run, the app will try to get the config.json file from the remote server,
if it fails, it will load it from the app's assets.

You can write your own backend server in order to test implementation (I did mine), and all
you have to do is to change the ip address on NetworkingModule.kt to your own.

Check my repo at https://github.com/itaishalom/SimpleServer for a simple config server.

The viewModel is initialised with OnActionSelected listener in order to create separation between the view
and the viewmodel.

If you wish to add more actions:
1. Add it to the Json file, the same format of the others.
2. Add the declaration on the OnActionSelectedListener interface
3. Implement on the view (MainActivity)
4. Add the case the "onActionButtonPressed" function at the viewModel

And that's it!

In order to test it, I created a MainViewModelTest class which runs to tests with a fake repository (In order 
to save time on DB actions/remote actions unneeded).
