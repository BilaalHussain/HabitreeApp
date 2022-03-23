// Based on https://stackoverflow.com/questions/55108949/how-can-i-send-an-image-file-from-firebase-functions

import * as functions from "firebase-functions";
import * as puppeteer from "puppeteer";

const runtimeOpts = {
  timeoutSeconds: 120,
  memory: <const> "1GB",
};

export const tree = functions.runWith(runtimeOpts).https.onRequest(
    async (request, response) => {
      try {
        const imageBuffer: Buffer = await generateScreenShot();
        response.writeHead(200, {"Content-Type": "image/png"});
        response.write(imageBuffer.toString("binary"), "binary");
        response.end();
      } catch (err) {
        console.error(err);
      }
    });

/** Function returns a screen of the tree page web app.
 *
 * @return {Buffer} Buffer with image
*/
function generateScreenShot(): Promise<Buffer> {
  return new Promise<Buffer>(async (resolve, reject) => {
    try {
      const browser = await puppeteer.launch({args: ["--no-sandbox"]});
      const page = await browser.newPage();
      await page.goto("https://cs446-habitree.web.app/", {
        waitUntil: "networkidle2",
      });
      await page.waitFor(500);
      const imageBuffer: any = await page.screenshot({fullPage: true});
      await browser.close();

      resolve(imageBuffer);
    } catch (err) {
      reject(err);
    }
  });
}
