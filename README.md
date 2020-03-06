 # Overview - DiscordBot
 
 This is a Discord-Bot with the following functionalities:
 - Blacklist
 - Link removal
 - RSS/ATOM Feed
 - Logging
 - Music playback
 - Level system
 - Command system
 - Permission system
 - OpenWeatherAPI integration
 
 ## Configuration
 
 Copy the `config/settings_default.json` file to `config/settings.json`.
 Open the copied file with your preferred editor.
 This is the default content of this file.
 ```
{
  "general":
  {
    "token": "",
    "token_encrypted": false,
    "owa-apikey": "",
    "default_rssdelay": 10,
    "default_prefix": "!"
  },

  "paths":
  {
    "database": "database/database.db"
  },

  "email":
  {
    "smtpHost": "",
    "smtpPort": "25",
    "fromEmail": "",
    "password": "",
    "toEmail": ""
  }
}
``` 
Insert your Discord-Token between the quotes at `token`.  
If you want to store your Discord-Token encrypted than specify it at the `token_encrypted` field. (But this only works if the token was encrypted with the onboard encryption function.)
If you have an OpenWeatherAPI-Key, insert it between the quotes at `owa-apikey`.  
The `default_rssdelay` specifies the delay in minutes between checks for RSS updates.  
The `default_prefix` specifies the default command prefix.  
If you want to use email notifications for error messages please enter your credentials in the `email` section. 

## Found a bug?

Feel free to open an issue or make a pull request.

## License

```
MIT License

Copyright (c) 2020 Fload

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

```